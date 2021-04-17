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
import java.io.IOException;
import java.text.MessageFormat;
import java.time.Duration;

import org.eclipse.jdt.launching.IVMInstall;

import com.link_intersystems.eclipse.core.runtime.runtime.IPluginLog;

import io.jitclipse.core.launch.VMVendor;

public abstract class AbstractShowVMSettingsVMVendorDiscoverer extends ProcessOutputGrabber
		implements IVMVendorDiscoverer {

	private IJavaExecutableLocator executableLocator = new DefaultJavaExecutableLocator();
	private IOutputGrabber outputGrabber = new ProcessOutputGrabber();
	private IPluginLog pluginLog;

	public AbstractShowVMSettingsVMVendorDiscoverer(IPluginLog pluginLog) {
		this.pluginLog = pluginLog;
	}

	void setOutputGrabber(IOutputGrabber outputGrabber) {
		this.outputGrabber = outputGrabber;
	}

	void setExecutableLocator(IJavaExecutableLocator executableLocator) {
		this.executableLocator = executableLocator;
	}

	@Override
	public VMVendor discover(IVMInstall vmInstall) {
		VMVendor vmVendor = null;

		File javaExec = executableLocator.locateExecutable(vmInstall);
		if (javaExec != null) {
			try {
				outputGrabber.setWait(Duration.ofSeconds(5L));

				String vmPropertiesOutput = outputGrabber.grabOutput(javaExec, "-XshowSettings:properties", "-version");
				vmVendor = discover(vmPropertiesOutput);
			} catch (IOException e) {
				String msg = MessageFormat.format("Unable to discover VM vendor", e);
				pluginLog.logError(msg, e);
			}
		}

		if (vmVendor == null) {
			vmVendor = VMVendor.UNKNOWN;
		}

		return vmVendor;
	}

	protected abstract VMVendor discover(String vmPropertiesOutput);

}