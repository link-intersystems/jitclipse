/*******************************************************************************
 * Copyright (c) 2021 Link Intersystems GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Link Intersystems GmbH - René Link - API and implementation
 *******************************************************************************/
package io.jitclipse.core.resources;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import io.jitclipse.core.jdt.JavaElementLocator;
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

	IProject getProject();

	public List<String> getSourceLocations();

	List<String> getBinaryResourceLocations();

	public JavaElementLocator getJavaElementLocator();
}
