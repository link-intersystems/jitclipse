package io.jitclipse.ui.preferences;

import com.link_intersystems.eclipse.core.preferences.IPreferences;
import com.link_intersystems.eclipse.core.preferences.Preference;

public interface IJitPreferences extends IPreferences {

	@Preference(value = PreferenceConstants.ASK_PERSPECTIVE_SWITCH, defaultValue = "true")
	public Boolean getAskPerspectiveSwitchEnabled();

	public void setAskPerspectiveSwitchEnabled(Boolean enabled);

}
