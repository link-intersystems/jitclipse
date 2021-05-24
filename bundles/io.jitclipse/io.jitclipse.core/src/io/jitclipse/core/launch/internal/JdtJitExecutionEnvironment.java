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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

import com.link_intersystems.eclipse.core.runtime.runtime.IExtensionPointProxyFactory;

import io.jitclipse.core.JitCorePlugin;
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

	public JdtJitExecutionEnvironment(ILaunchConfiguration configuration, IFile hotspotLogFile) throws CoreException {
		this.configuration = configuration;
		this.hotspotLogFile = hotspotLogFile;
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
			if (idIgnoreCase.startsWith("javase-") || idIgnoreCase.startsWith("jre-")
					|| idIgnoreCase.startsWith("j2se-")) {
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

		boolean hsdisLibraryApplied = applyHsdisLibrary(workingCopy);
		applyJitArgs(jitArgs, workingCopy, hsdisLibraryApplied);
	}

	private void applyJitArgs(IJitArgs jitArgs, ILaunchConfigurationWorkingCopy workingCopy,
			boolean hsdisLibraryApplied) throws CoreException {
		String arguments = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, ""); //$NON-NLS-1$

		StringBuilder sb = new StringBuilder(arguments);

		IPath location = hotspotLogFile.getLocation();
		jitArgs.setHotspotLogFile(location.toFile());
		jitArgs.setDisassembledCodeEnabled(hsdisLibraryApplied);
		jitArgs.setClassModelEnabled(true);

		if (!jitArgs.isEmpty()) {
			sb.append(' ');
			sb.append(jitArgs);
		}

		String jitEnabledArguments = sb.toString();
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, jitEnabledArguments);
	}

	private boolean applyHsdisLibrary(ILaunchConfigurationWorkingCopy workingCopy) throws CoreException {
		Env env = Env.getCurrent().get();

		Map<String, String> envVariables = configuration.getAttribute(ILaunchManager.ATTR_ENVIRONMENT_VARIABLES,
				new HashedMap<>());
		String path = envVariables.getOrDefault("PATH", "");
		EnvPath envPath = env.parsePath(path);
		boolean hsdisApplied = applyHsdis(env, envPath);

		String effectivePath = env.formatPath(envPath);
		envVariables.put("PATH", effectivePath);
		workingCopy.setAttribute(ILaunchManager.ATTR_ENVIRONMENT_VARIABLES, envVariables);
		workingCopy.setAttribute(ILaunchManager.ATTR_APPEND_ENVIRONMENT_VARIABLES, true);

		return hsdisApplied;
	}

	private boolean applyHsdis(Env env, EnvPath envPath) {
		int sizeBefore = envPath.size();

		JitCorePlugin jitCorePlugin = JitCorePlugin.getInstance();
		IExtensionPointProxyFactory extensionsPointProxyFactory = jitCorePlugin.getExtensionsPointProxyFactory();
		List<HsdisProviderExtension> hsdisProviderExtensions = extensionsPointProxyFactory
				.createProxies(HsdisProviderExtension.class);
		for (HsdisProviderExtension hsdisProviderExtension : hsdisProviderExtensions) {
			HsdisProvider provider = hsdisProviderExtension.getProvider();
			File hsdisLibraryFolder = provider.getHsdisLibraryFolder(env);
			if (hsdisLibraryFolder != null) {
				String absoluteHsdisLibraryPath = hsdisLibraryFolder.getAbsolutePath();
				envPath.add(absoluteHsdisLibraryPath);
			}
		}

		return sizeBefore < envPath.size();
	}

}
