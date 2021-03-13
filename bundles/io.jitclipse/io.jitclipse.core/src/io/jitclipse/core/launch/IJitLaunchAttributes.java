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
