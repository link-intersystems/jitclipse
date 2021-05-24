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
package io.jitclipse.core.launch.internal;

import static io.jitclipse.core.JitCorePlugin.LAUNCH_MODE;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.Launch;

import com.link_intersystems.eclipse.core.runtime.runtime.IPluginLog;

import io.jitclipse.core.jdt.launching.JdtJitExecutionEnvironment;
import io.jitclipse.core.launch.IJitArgs;
import io.jitclipse.core.launch.IJitArgsProvider;
import io.jitclipse.core.launch.IJitLaunch;
import io.jitclipse.core.resources.IHotspotLogFile;
import io.jitclipse.core.resources.IHotspotLogFolder;
import io.jitclipse.core.resources.IJitProject;

public class JitLaunch extends Launch implements IJitLaunch {

	private static final String ATTR_HOTSPOT_LOG_FILE_LOCATION = IJitLaunch.class.getCanonicalName()
			+ ".hotspotLogFileLocation";

	private IFile hotspotLogFile;
	private IProject project;

	private IJitArgsProvider argsProvider;

	public JitLaunch(ILaunchConfiguration launchConfiguration, IJitArgsProvider argsProvider, IPluginLog pluginLog) {
		super(launchConfiguration, LAUNCH_MODE, null);
		this.argsProvider = argsProvider;

		String projectName;
		try {
			projectName = launchConfiguration.getAttribute("org.eclipse.jdt.launching.PROJECT_ATTR", (String) null);
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			project = root.getProject(projectName);

			IJitProject jitProject = project.getAdapter(IJitProject.class);
			IHotspotLogFolder hotspotLogFolder = jitProject.getHotspotLogFolder();
			hotspotLogFile = hotspotLogFolder.newHotspotLogFile();

			setAttribute(ATTR_HOTSPOT_LOG_FILE_LOCATION, hotspotLogFile.getLocation().toOSString());
		} catch (CoreException e) {
			pluginLog.logError(e);
		}

	}

	@Override
	public IHotspotLogFile getHotspotLogFile() {
		try {
			hotspotLogFile.getParent().refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
		}
		return hotspotLogFile.getAdapter(IHotspotLogFile.class);
	}

	public ILaunchConfiguration getEffectiveLaunchConfiguration() throws CoreException {
		ILaunchConfiguration configuration = getLaunchConfiguration();
		ILaunchConfigurationWorkingCopy workingCopy = configuration.getWorkingCopy();

		if (hotspotLogFile != null) {
			JdtJitExecutionEnvironment jitExecutionEnvironment = new JdtJitExecutionEnvironment(configuration, hotspotLogFile);
			IJitArgs jitArgs = argsProvider.createJitArgs(jitExecutionEnvironment);

			jitExecutionEnvironment.apply(jitArgs, workingCopy);
		}

		return workingCopy;
	}

	@Override
	public IProject getProject() {
		return project;
	}

}
