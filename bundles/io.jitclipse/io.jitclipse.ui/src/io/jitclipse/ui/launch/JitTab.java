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


import static io.jitclipse.core.launch.IJitLaunchAttributes.JRE_OPTIONS_JITCLIPSE;
import static io.jitclipse.core.launch.IJitLaunchAttributes.JRE_OPTIONS_MANUAL;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.link_intersystems.eclipse.ui.swt.events.SelectionMethods;

import io.jitclipse.core.launch.IJitLaunchAttributes;

public class JitTab extends AbstractLaunchConfigurationTab {

	private IJitLaunchAttributes launchAttributes;
	private Button jitclipseOptionsButton;
	private Button manualOptionsButton;

	public void createControl(Composite parent) {
		parent = new Composite(parent, SWT.NONE);
		parent.setLayout(new GridLayout(1, false));

		jitclipseOptionsButton = new Button(parent, SWT.RADIO);
		jitclipseOptionsButton.setText("Let JITclipse choose JRE options");
		jitclipseOptionsButton
				.addSelectionListener(SelectionMethods.ANY.listener(() -> setJreOption(JRE_OPTIONS_JITCLIPSE)));

		manualOptionsButton = new Button(parent, SWT.RADIO);
		manualOptionsButton
				.addSelectionListener(SelectionMethods.ANY.listener(() -> setJreOption(JRE_OPTIONS_MANUAL)));
		manualOptionsButton.setText("Manually set JRE options (use the arguments tab.)");

		setControl(parent);
	}

	private void setJreOption(String name) {
		if (launchAttributes == null) {
			return;
		}

		launchAttributes.setJREOptions(name);

		setDirty(launchAttributes.isDirty());

		updateLaunchConfigurationDialog();
	}

	private void update() {
		String jreOptions = launchAttributes.getJREOptions();
		if (JRE_OPTIONS_JITCLIPSE.equals(jreOptions)) {
			jitclipseOptionsButton.setSelection(true);
			manualOptionsButton.setSelection(false);
		} else {
			jitclipseOptionsButton.setSelection(false);
			manualOptionsButton.setSelection(true);
		}
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		IJitLaunchAttributes launchAttributes = IJitLaunchAttributes.createInstance(configuration);
		launchAttributes.setJREOptions(JRE_OPTIONS_JITCLIPSE);
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		updateErrorStatus();

		launchAttributes = IJitLaunchAttributes.createInstance(configuration);
		setDirty(launchAttributes.isDirty());
		update();
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
		return "JITclipse";
	}

	@Override
	public Image getImage() {
		return null;
	}

	private void updateErrorStatus() {
	}

	@Override
	public void dispose() {
		jitclipseOptionsButton = null;
		manualOptionsButton = null;
	}

}
