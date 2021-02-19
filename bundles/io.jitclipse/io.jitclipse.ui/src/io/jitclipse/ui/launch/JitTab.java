/*******************************************************************************
 * Copyright (c) 2006, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 ******************************************************************************/
package io.jitclipse.ui.launch;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * The "Coverage" tab of the launch configuration dialog.
 */
public class JitTab extends AbstractLaunchConfigurationTab {

	public JitTab() {
	}

	public void createControl(Composite parent) {
		parent = new Composite(parent, SWT.NONE);
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// nothing to do
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		updateErrorStatus();
		setDirty(false);
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		if (isDirty()) {
		}
	}

	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		return true;
	}

	public String getName() {
		return "Jit Watch";
	}

	@Override
	public Image getImage() {
		return null;
	}

	private void updateErrorStatus() {
	}

}
