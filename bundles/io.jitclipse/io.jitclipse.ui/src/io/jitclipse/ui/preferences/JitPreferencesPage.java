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
package io.jitclipse.ui.preferences;

import static io.jitclipse.ui.preferences.PreferenceConstants.ASK_PERSPECTIVE_SWITCH;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import io.jitclipse.ui.JitUIPlugin;

public class JitPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private BooleanFieldEditor askPerspectiveSwitchField;

	public JitPreferencesPage() {
		super(GRID);
		setTitle("JIT Preferences");
		setPreferenceStore(JitUIPlugin.getInstance().getPreferenceStore());
		setDescription("");
	}

	public void createFieldEditors() {
		askPerspectiveSwitchField = new BooleanFieldEditor(ASK_PERSPECTIVE_SWITCH, "Always ask before opening the hotspot &perspective.", getFieldEditorParent());
		addField(askPerspectiveSwitchField);
	}

	@Override
	public boolean performOk() {
		askPerspectiveSwitchField.store();
		return super.performOk();
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}