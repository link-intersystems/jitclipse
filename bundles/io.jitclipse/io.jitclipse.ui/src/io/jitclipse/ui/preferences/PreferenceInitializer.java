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

import com.link_intersystems.eclipse.core.runtime.preferences.AbstractIPreferencesInitializer;
import com.link_intersystems.eclipse.core.runtime.preferences.IPreferences;
import com.link_intersystems.eclipse.core.runtime.runtime.IPluginContext;

import io.jitclipse.ui.JitUIPlugin;

public class PreferenceInitializer extends AbstractIPreferencesInitializer {

	public PreferenceInitializer() {
		this(JitUIPlugin.getInstance(), IJitPreferences.class);
	}

	protected PreferenceInitializer(IPluginContext pluginContext, Class<? extends IPreferences> preferencesInterface) {
		super(pluginContext, preferencesInterface);
	}

}
