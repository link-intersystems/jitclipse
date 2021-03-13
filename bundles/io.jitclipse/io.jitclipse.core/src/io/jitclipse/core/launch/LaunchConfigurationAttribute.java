package io.jitclipse.core.launch;

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

public class LaunchConfigurationAttribute {

	private ILaunchConfigurationWorkingCopy writeableConfig;
	private ILaunchConfiguration readableConfig;
	private String attributeName;

	public LaunchConfigurationAttribute(ILaunchConfiguration readableConfig, String attributeName) {
		this.readableConfig = readableConfig;
		this.attributeName = attributeName;

	}

	public LaunchConfigurationAttribute(ILaunchConfigurationWorkingCopy writeableConfig, String attributeName) {
		this((ILaunchConfiguration) writeableConfig, attributeName);
		this.writeableConfig = writeableConfig;
	}

	public void setString(String value) {
		if (writeableConfig == null) {
			String msg = MessageFormat.format("LaunchConfiguration attribute {0} is not writeable.", attributeName);
			throw new UnsupportedOperationException(msg);
		}

		writeableConfig.setAttribute(attributeName, value);
	}

	public String getString() {
		try {
			return readableConfig.getAttribute(attributeName, (String) null);
		} catch (CoreException e) {
			return null;
		}
	}

	public String getString(String defaultValue) {
		try {
			return readableConfig.getAttribute(attributeName, defaultValue);
		} catch (CoreException e) {
			return null;
		}
	}
}