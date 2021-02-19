package io.jitclipse.jitwatch.core.model;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdapterFactory;

import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IPackage;
import io.jitclipse.core.resources.IHotspotLogFile;
import io.jitclipse.core.resources.IHotspotLogFolder;
import io.jitclipse.core.resources.IJitProject;

public class PackageAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, java.lang.Class<T> adapterType) {
		if (IPackage.class.isInstance(adaptableObject)) {
			IPackage packageObj = IPackage.class.cast(adaptableObject);
			return getAdapter(packageObj, adapterType);
		}
		return null;
	}

	public <T> T getAdapter(IPackage packageObj, java.lang.Class<T> adapterType) {
		if (IHotspotLog.class.isAssignableFrom(adapterType)) {
			return adapterType.cast(getHotspotLog(packageObj));
		}
		return null;

	}

	@Override
	public java.lang.Class<?>[] getAdapterList() {
		return new java.lang.Class<?>[] { IHotspotLog.class };
	}

	private IHotspotLog getHotspotLog(IPackage packageObj) {

		IPackage packageRoot = packageObj.getRoot();

		IHotspotLog hotspotLog = null;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

		IProject[] projects = root.getProjects();
		projects: for (IProject project : projects) {
			IJitProject jitProject = project.getAdapter(IJitProject.class);

			if (jitProject != null) {
				IHotspotLogFolder hotspotLogFolder = jitProject.getHotspotLogFolder();
				List<IHotspotLogFile> hotspotLogfiles = hotspotLogFolder.getHotspotLogfiles();
				for (IHotspotLogFile hotspotLogFile : hotspotLogfiles) {
					IHotspotLog currentHotspotLog = hotspotLogFile.getHotspotLog();
					if (currentHotspotLog != null) {
						if (currentHotspotLog.contains(packageRoot)) {
							hotspotLog = currentHotspotLog;
							break projects;
						}
					}
				}

			}
		}
		return hotspotLog;
	}
}
