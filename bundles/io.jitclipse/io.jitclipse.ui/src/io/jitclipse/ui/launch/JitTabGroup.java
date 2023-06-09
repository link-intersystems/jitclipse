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
package io.jitclipse.ui.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;

import io.jitclipse.ui.JitUIPlugin;

public class JitTabGroup implements ILaunchConfigurationTabGroup, IExecutableExtension {

	private static final String DELEGATE_LAUNCHMODE = ILaunchManager.RUN_MODE;
	private static final String EXPOINT_TABGROUP = "org.eclipse.debug.ui.launchConfigurationTabGroups"; //$NON-NLS-1$
	private static final String CONFIGATTR_TYPE = "type"; //$NON-NLS-1$

	private ILaunchConfigurationTabGroup tabGroupDelegate;
	private ILaunchConfigurationTab jitTab;

	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		tabGroupDelegate = createDelegate(config.getAttribute(CONFIGATTR_TYPE));
	}

	protected ILaunchConfigurationTabGroup createDelegate(String type) throws CoreException {
		IExtensionPoint extensionpoint = Platform.getExtensionRegistry().getExtensionPoint(EXPOINT_TABGROUP);
		IConfigurationElement[] tabGroupConfigs = extensionpoint.getConfigurationElements();
		IConfigurationElement element = null;
		findloop: for (IConfigurationElement tabGroupConfig : tabGroupConfigs) {
			if (type.equals(tabGroupConfig.getAttribute(CONFIGATTR_TYPE))) {
				IConfigurationElement[] modeConfigs = tabGroupConfig.getChildren("launchMode"); //$NON-NLS-1$
				if (modeConfigs.length == 0) {
					element = tabGroupConfig;
				}
				for (final IConfigurationElement config : modeConfigs) {
					if (DELEGATE_LAUNCHMODE.equals(config.getAttribute("mode"))) { //$NON-NLS-1$
						element = tabGroupConfig;
						break findloop;
					}
				}
			}
		}
		if (element == null) {
			String msg = "No tab group registered to run " + type; //$NON-NLS-1$
			throw new CoreException(JitUIPlugin.errorStatus(msg, null));
		} else {
			return (ILaunchConfigurationTabGroup) element.createExecutableExtension("class"); //$NON-NLS-1$
		}
	}

	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		tabGroupDelegate.createTabs(dialog, mode);
		jitTab = createJitTab(dialog, mode);
	}

	protected ILaunchConfigurationTab createJitTab(ILaunchConfigurationDialog dialog, String mode) {
		return new JitTab();
	}

	public ILaunchConfigurationTab[] getTabs() {
		return insertJitTab(tabGroupDelegate.getTabs(), jitTab);
	}

	protected ILaunchConfigurationTab[] insertJitTab(ILaunchConfigurationTab[] delegateTabs,
			ILaunchConfigurationTab jitTab) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[delegateTabs.length + 1];
		tabs[0] = delegateTabs[0];
		tabs[1] = jitTab;
		System.arraycopy(delegateTabs, 1, tabs, 2, delegateTabs.length - 1);
		return tabs;
	}

	public void dispose() {
		tabGroupDelegate.dispose();
		jitTab.dispose();
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		tabGroupDelegate.setDefaults(configuration);
		jitTab.setDefaults(configuration);
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		tabGroupDelegate.initializeFrom(configuration);
		jitTab.initializeFrom(configuration);
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		tabGroupDelegate.performApply(configuration);
		jitTab.performApply(configuration);
	}

	public void launched(ILaunch launch) {
		// deprecated method will not be called
	}

}
