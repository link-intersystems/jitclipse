package io.jitclipse.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.model.WorkbenchContentProvider;

import com.link_intersystems.eclipse.core.runtime.runtime.progress.IProgress;
import com.link_intersystems.eclipse.core.runtime.runtime.progress.ProgressIndicator;
import com.link_intersystems.eclipse.ui.swt.widgets.Display2;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IPackage;
import io.jitclipse.core.resources.IHotspotLogFile;
import io.jitclipse.core.resources.IHotspotLogFolder;
import io.jitclipse.core.resources.IJitProject;

public class JitNavigatorContentProvider extends WorkbenchContentProvider {

	private StructuredViewer viewer;

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
		this.viewer = (StructuredViewer) viewer;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (IProject.class.isInstance(inputElement)) {
			IProject project = IProject.class.cast(inputElement);
			IJitProject jitProject = project.getAdapter(IJitProject.class);

			if (jitProject != null) {
				return getElements(jitProject);
			}
		}
		return null;
	}

	public Object[] getElements(IJitProject jitProject) {
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		List<Object> children = new ArrayList<>();

		if (IProject.class.isInstance(parentElement)) {
			IProject project = IProject.class.cast(parentElement);
			IJitProject jitProject = project.getAdapter(IJitProject.class);

			if (jitProject != null) {
				IHotspotLogFolder hotspotLogFolder = jitProject.getHotspotLogFolder();

				if (hotspotLogFolder.exists()) {
					children.add(hotspotLogFolder);
				}
			}
		} else if (IHotspotLogFolder.class.isInstance(parentElement)) {
			IHotspotLogFolder hotspotLogFileFolder = IHotspotLogFolder.class.cast(parentElement);
			List<IFile> files = hotspotLogFileFolder.getFiles();
			children.addAll(files);
		} else if (IFile.class.isInstance(parentElement)) {
			IFile file = IFile.class.cast(parentElement);
			IHotspotLogFile hotspotLogFile = file.getAdapter(IHotspotLogFile.class);
			if (hotspotLogFile != null) {

				IProgress progress = hotspotLogFile.getProgress();
				if (progress != null) {
					children.add(progress);
				}

				IHotspotLog hotspotLog = hotspotLogFile.getHotspotLog();
				if (hotspotLog != null) {
					List<IPackage> rootPackages = hotspotLog.getRootPackages();
					children.addAll(rootPackages);
				}
			}
		}

		return children.toArray();
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IMethod) {
			IMethod method = (IMethod) element;
			return method.getType();
		} else if (element instanceof IClass) {
			IClass type = (IClass) element;
			return type.getPackage();
		} else if (element instanceof IPackage) {
			IPackage aPackage = (IPackage) element;
			IPackage parent = aPackage.getParent();
			boolean rootPackage = parent == null;

			if (rootPackage) {
				List<IJitProject> projects = IJitProject.getOpenJitWatchProjects();
				for (IJitProject jitProject : projects) {
					IHotspotLogFile hotspotLogFile = jitProject.getHotspotLogFile(aPackage);
					if (hotspotLogFile != null) {
						return hotspotLogFile.getFile();
					}

				}
			} else {
				return parent;
			}
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ProgressIndicator) {
			return false;
		} else if (element instanceof IFile) {
			IFile file = (IFile) element;
			IHotspotLogFile hotspotLogFile = file.getAdapter(IHotspotLogFile.class);
			return hotspotLogFile != null;
		}
		return true;
	}

	@Override
	protected void processDelta(IResourceDelta delta) {
		super.processDelta(delta);

		Control ctrl = viewer.getControl();
		if (ctrl == null || ctrl.isDisposed()) {
			return;
		}

		IResourceDelta[] affectedChildren = delta.getAffectedChildren();
		for (IResourceDelta affectedChild : affectedChildren) {
			IResource resource = affectedChild.getResource();
			IJitProject jitProject = resource.getAdapter(IJitProject.class);
			if (jitProject != null) {
				Display2.asyncExec(viewer::refresh, resource);
			}
		}
	}

}
