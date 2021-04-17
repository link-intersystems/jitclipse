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
package io.jitclipse.core.launch;

public class BooleanJavaOption extends JavaOption {

	private static final String ENABLED_FLAG = "+";
	private static final String DISABLED_FLAG = "-";

	private boolean enabled;

	public BooleanJavaOption(String name) {
		super(name);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		return "-XX:" + getBooleanFlag() + getName();
	}

	private String getBooleanFlag() {
		return isEnabled() ? ENABLED_FLAG : DISABLED_FLAG;
	}

}
