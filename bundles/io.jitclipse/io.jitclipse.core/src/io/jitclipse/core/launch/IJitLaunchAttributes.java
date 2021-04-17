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
package io.jitclipse.core.launch;

import org.eclipse.debug.core.ILaunchConfiguration;

import io.jitclipse.core.launch.config.LaunchAttribute;
import io.jitclipse.core.launch.config.LaunchConfigurationProxySupport;

public interface IJitLaunchAttributes {

	public static final String JRE_OPTIONS_JITCLIPSE = "JITCLIPSE";
	public static final String JRE_OPTIONS_MANUAL = "MANUAL";

	public static IJitLaunchAttributes createInstance(ILaunchConfiguration launchConfiguration) {
		LaunchConfigurationProxySupport<IJitLaunchAttributes> launchConfigurationProxySupport = new LaunchConfigurationProxySupport<>(
				IJitLaunchAttributes.class, launchConfiguration);
		return launchConfigurationProxySupport.createProxy();
	}

	@LaunchAttribute(defaultValue = JRE_OPTIONS_JITCLIPSE)
	String getJREOptions();

	void setJREOptions(String options);


	public boolean isDirty();

}
