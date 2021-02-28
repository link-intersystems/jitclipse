package io.jitclipse.core.launch.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.launch.VMVendor;

public class DefaultShowSettingsVMVendorDiscoverer extends AbstractShowVMSettingsVMVendorDiscoverer {

	public DefaultShowSettingsVMVendorDiscoverer() {
		super(JitCorePlugin.getInstance().getPluginLog());
	}

	@Override
	protected VMVendor discover(String vmPropertiesOutput) {
		Pattern vendorPattern = Pattern.compile("^\\s*java\\.vendor\\s*=\\s*(.*)$", Pattern.MULTILINE);
		Matcher matcher = vendorPattern.matcher(vmPropertiesOutput);
		if (matcher.find()) {
			String vendorGroup = matcher.group(1);
			if (vendorGroup.toLowerCase().contains("oracle")) {
				return VMVendor.ORACLE;
			}
		}

		return null;
	}

}
