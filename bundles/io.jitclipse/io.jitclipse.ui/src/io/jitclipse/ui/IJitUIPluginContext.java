package io.jitclipse.ui;

import com.link_intersystems.eclipse.ui.plugin.IUIPluginContext;

import io.jitclipse.ui.preferences.IJitPreferences;

public interface IJitUIPluginContext extends IUIPluginContext {

	IJitPreferences getJitPreferences();

}
