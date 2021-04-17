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
