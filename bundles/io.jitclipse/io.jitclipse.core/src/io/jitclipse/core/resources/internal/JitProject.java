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
package io.jitclipse.core.resources.internal;

import static org.eclipse.jdt.core.IClasspathEntry.CPE_LIBRARY;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.link_intersystems.eclipse.core.runtime.runtime.IPluginLog;

import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.JitPluginContext;
import io.jitclipse.core.parser.IJitLogParser;
import io.jitclipse.core.resources.IHotspotLogFolder;
import io.jitclipse.core.resources.IJitProject;

public class JitProject implements IJitProject {

	private HotspotLogFolder hotspotLogFileManager;
	private JitPluginContext jitPluginContext;
	private IProject project;

	public JitProject(IProject project) {
		this(JitCorePlugin.getInstance(), Clock.systemDefaultZone(), project);
	}

	public JitProject(JitPluginContext jitPluginContext, Clock clock, IProject project) {
		this.jitPluginContext = jitPluginContext;
		this.project = project;
		hotspotLogFileManager = new HotspotLogFolder(clock, project);
	}

	@Override
	public IHotspotLogFolder getHotspotLogFolder() {
		return hotspotLogFileManager;
	}

	@Override
	public IJitLogParser getParser() throws CoreException {
		return jitPluginContext.getJitLogParser();
	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public List<String> getSourceLocations() {
		List<String> sourceLocations = new ArrayList<>();
		IJavaProject javaProject = JavaCore.create(project);

		try {
			IClasspathEntry[] resolvedClasspath = javaProject.getResolvedClasspath(false);

			IWorkspace workspace = project.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();

			for (IClasspathEntry classpathEntry : resolvedClasspath) {
				IPath path = classpathEntry.getPath();

				int entryKind = classpathEntry.getEntryKind();
				switch (entryKind) {
				case IClasspathEntry.CPE_SOURCE:
					IFolder sourceFolder = root.getFolder(path);
					IPath sourceLocation = sourceFolder.getLocation();
					String sourceLocationOsString = sourceLocation.toOSString();
					sourceLocations.add(sourceLocationOsString);
					break;
				}
			}
		} catch (JavaModelException e) {
			IPluginLog pluginLog = jitPluginContext.getPluginLog();
			pluginLog.logError("Unable to resolve source folders for " + project, e);
		}

		return sourceLocations;
	}

	@Override
	public List<String> getBinaryResourceLocations() {
		List<String> binaryResources = new ArrayList<>();
		IJavaProject javaProject = JavaCore.create(project);

		try {
			IClasspathEntry[] resolvedClasspath = javaProject.getResolvedClasspath(false);
			IPath outputLocation = javaProject.getOutputLocation();

			IWorkspace workspace = project.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			IFolder folder = root.getFolder(outputLocation);
			IPath fullPath = folder.getLocation();
			binaryResources.add(fullPath.toOSString());

			for (IClasspathEntry classpathEntry : resolvedClasspath) {
				int entryKind = classpathEntry.getEntryKind();
				switch (entryKind) {
				case CPE_LIBRARY:
					IPath libraryPath = classpathEntry.getPath();
					String libraryOsString = libraryPath.toOSString();
					binaryResources.add(libraryOsString);
					break;
				}

			}
		} catch (JavaModelException e) {
			IPluginLog pluginLog = jitPluginContext.getPluginLog();
			pluginLog.logError("Unable to resolve binary resources for " + project, e);
		}

		return binaryResources;
	}

}
