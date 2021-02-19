package io.jitclipse.ui.preferences;

import com.link_intersystems.eclipse.core.preferences.AbstractIPreferencesInitializer;
import com.link_intersystems.eclipse.core.preferences.IPreferences;
import com.link_intersystems.eclipse.core.runtime.IPluginContext;

import io.jitclipse.ui.JitUIPlugin;

public class PreferenceInitializer extends AbstractIPreferencesInitializer {

	public PreferenceInitializer() {
		this(JitUIPlugin.getInstance(), IJitPreferences.class);
	}

	protected PreferenceInitializer(IPluginContext pluginContext, Class<? extends IPreferences> preferencesInterface) {
		super(pluginContext, preferencesInterface);
	}

}
