package io.jitclipse.core.resources.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.JavaCore;

import com.link_intersystems.eclipse.core.runtime.runtime.PluginLog;

import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.resources.IJitProject;

public class ProjectAdapterFactory implements IAdapterFactory {

	private static final QualifiedName JIT_WATCH_PROJECT = new QualifiedName(JitCorePlugin.ID, "jitProject");

	private PluginLog pluginLog = JitCorePlugin.getInstance().getPluginLog();

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (IProject.class.isInstance(adaptableObject)) {
			return getAdapter(IProject.class.cast(adaptableObject), adapterType);
		}
		return null;
	}

	private <T> T getAdapter(IProject project, Class<T> adapterType) {
		try {
			if (project.hasNature(JavaCore.NATURE_ID)) {
				if (IJitProject.class.isAssignableFrom(adapterType)) {

					try {
						IJitProject jitProject = (IJitProject) project
								.getSessionProperty(JIT_WATCH_PROJECT);

						if (jitProject == null) {
							jitProject = new JitProject(project);
							project.setSessionProperty(JIT_WATCH_PROJECT, jitProject);
						}

						return adapterType.cast(jitProject);
					} catch (CoreException e) {
						pluginLog.logError(e);
					}
				}
			}
		} catch (CoreException e) {
			pluginLog.logError(e);
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class<?>[] { IJitProject.class };
	}

}
