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