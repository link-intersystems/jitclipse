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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.eclipse.jdt.launching.environments.IExecutionEnvironmentsManager;

import io.jitclipse.core.launch.IJitExecutionEnvironment;
import io.jitclipse.core.launch.VMVendor;

public class JdtJitExecutionEnvironment implements IJitExecutionEnvironment {

	private IVMInstall vmInstall;
	private IVMVendorDiscoverer vendorDiscoverer = new DefaultShowSettingsVMVendorDiscoverer();
	private VMVendor vendor;

	public JdtJitExecutionEnvironment(ILaunchConfiguration configuration) throws CoreException {
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
}
