package io.jitclipse.core.resources;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import io.jitclipse.core.model.IPackage;
import io.jitclipse.core.parser.IJitLogParser;

public interface IJitProject {

	public IHotspotLogFolder getHotspotLogFolder();

	public IJitLogParser getParser() throws CoreException;

	public static List<IJitProject> getOpenJitWatchProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		return getOpenJitWatchProjects(projects);
	}

	public static List<IJitProject> getOpenJitWatchProjects(IProject[] projects) {
		List<IJitProject> openProjects = new ArrayList<>();
		for (IProject project : projects) {
			if (project.isOpen()) {
				IJitProject jitProject = project.getAdapter(IJitProject.class);
				if (jitProject != null) {
					openProjects.add(jitProject);
				}
			}
		}
		return openProjects;
	}

	default public IHotspotLogFile getHotspotLogFile(IPackage aPackage) {
		IHotspotLogFolder hotspotLogFolder = getHotspotLogFolder();
		return hotspotLogFolder.getHotspotLogFile(aPackage);
	}
}
