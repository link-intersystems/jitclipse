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
package io.jitclipse.core.jdt.launching;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.map.HashedMap;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.eclipse.jdt.launching.environments.IExecutionEnvironmentsManager;

import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.jdt.launch.DefaultShowSettingsVMVendorDiscoverer;
import io.jitclipse.core.launch.Env;
import io.jitclipse.core.launch.EnvPath;
import io.jitclipse.core.launch.HsdisProvider;
import io.jitclipse.core.launch.IJitArgs;
import io.jitclipse.core.launch.IJitExecutionEnvironment;
import io.jitclipse.core.launch.VMVendor;

public class JdtJitExecutionEnvironment implements IJitExecutionEnvironment {

	private IVMInstall vmInstall;
	private IVMVendorDiscoverer vendorDiscoverer = new DefaultShowSettingsVMVendorDiscoverer();
	private VMVendor vendor;
	private IFile hotspotLogFile;
	private ILaunchConfiguration configuration;
	private HsdisProvider hsdisProvider;

	public JdtJitExecutionEnvironment(ILaunchConfiguration configuration, IFile hotspotLogFile) throws CoreException {
		this(configuration, hotspotLogFile, JitCorePlugin.getInstance().getHsdisProvider());
	}

	public JdtJitExecutionEnvironment(ILaunchConfiguration configuration, IFile hotspotLogFile,
			HsdisProvider hsdisProvider) throws CoreException {
		this.configuration = configuration;
		this.hotspotLogFile = hotspotLogFile;
		this.hsdisProvider = hsdisProvider;
		vmInstall = JavaRuntime.computeVMInstall(configuration);
	}

	private IExecutionEnvironment getExecutionEnvironment(IVMInstall vmInstall) {
		IExecutionEnvironmentsManager executionEnvironmentsManager = JavaRuntime.getExecutionEnvironmentsManager();
		IExecutionEnvironment[] executionEnvironments = executionEnvironmentsManager.getExecutionEnvironments();

		IExecutionEnvironment vmInstallEnvironment = null;

		List<IExecutionEnvironment> possibleEnvironments = new ArrayList<>();

		for (IExecutionEnvironment executionEnvironment : executionEnvironments) {
			String id = executionEnvironment.getId();
			String idIgnoreCase = id.toLowerCase();
			if (idIgnoreCase.startsWith("javase-") || idIgnoreCase.startsWith("jre-") //$NON-NLS-1$
					|| idIgnoreCase.startsWith("j2se-")) { //$NON-NLS-1$
				IVMInstall[] compatibleVMs = executionEnvironment.getCompatibleVMs();
				if (Arrays.asList(compatibleVMs).contains(vmInstall)) {
					possibleEnvironments.add(executionEnvironment);
				}
			}
		}

		Collections.sort(possibleEnvironments, new ExecutionEnvironmentVersionComparator().reversed());

		if (!possibleEnvironments.isEmpty()) {
			vmInstallEnvironment = possibleEnvironments.get(0);
		}

		return vmInstallEnvironment;
	}

	@Override
	public VMVendor getVMVendor() {
		if (vendor == null) {
			vendor = vendorDiscoverer.discover(vmInstall);
		}
		return vendor;

	}

	@Override
	public String getExecutionEnvironmentId() {
		IExecutionEnvironment executionEnvironment = getExecutionEnvironment(vmInstall);
		if (executionEnvironment != null) {
			return executionEnvironment.getId();
		}
		return null;
	}

	public void apply(IJitArgs jitArgs, ILaunchConfigurationWorkingCopy workingCopy) throws CoreException {

		applyJitArgs(jitArgs, workingCopy);
		boolean hsdisLibraryApplied = applyHsdisLibrary(workingCopy);
		jitArgs.setDisassembledCodeEnabled(hsdisLibraryApplied);
	}

	private void applyJitArgs(IJitArgs jitArgs, ILaunchConfigurationWorkingCopy workingCopy) throws CoreException {
		String configuredArgs = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, ""); //$NON-NLS-1$

		StringBuilder sb = new StringBuilder(configuredArgs);

		IPath location = hotspotLogFile.getLocation();
		jitArgs.setHotspotLogFile(location.toFile());
		jitArgs.setClassModelEnabled(true);

		if (!jitArgs.isEmpty()) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(jitArgs);
		}

		String jitEnabledArguments = sb.toString();
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, jitEnabledArguments);
	}

	private boolean applyHsdisLibrary(ILaunchConfigurationWorkingCopy workingCopy) throws CoreException {
		Optional<Env> envOptional = Env.getCurrent();
		if (envOptional.isEmpty()) {
			return false;
		}

		Env env = envOptional.get();

		Map<String, String> envVariables = configuration.getAttribute(ILaunchManager.ATTR_ENVIRONMENT_VARIABLES,
				new HashedMap<>());
		String path = envVariables.getOrDefault("PATH", ""); //$NON-NLS-1$
		EnvPath envPath = env.parsePath(path);
		boolean hsdisApplied = applyHsdis(env, envPath);

		String effectivePath = env.formatPath(envPath);
		envVariables.put("PATH", effectivePath); //$NON-NLS-1$
		workingCopy.setAttribute(ILaunchManager.ATTR_ENVIRONMENT_VARIABLES, envVariables);

		return hsdisApplied;
	}

	private boolean applyHsdis(Env env, EnvPath envPath) {
		int pathElementsBefore = envPath.size();

		Optional<File> hsdisLibraryFolder = hsdisProvider.getHsdisLibraryFolder(env);
		hsdisLibraryFolder.map(File::getAbsolutePath).ifPresent(envPath::add);

		int pathElementsAfter = envPath.size();
		boolean envPathChanged = pathElementsAfter > pathElementsBefore;
		return envPathChanged;
	}

}
