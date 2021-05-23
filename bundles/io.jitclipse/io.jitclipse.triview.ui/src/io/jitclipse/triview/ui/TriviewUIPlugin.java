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
package io.jitclipse.triview.ui;

import org.osgi.framework.BundleContext;

import com.link_intersystems.eclipse.ui.jface.plugin.AbstractJFacePlugin;

public class TriviewUIPlugin extends AbstractJFacePlugin {

	public static final String ID = "io.jitclipse.triview.ui";

	private static TriviewUIPlugin inst;

	public static TriviewUIPlugin getInstance() {
		return inst;
	}

	public TriviewUIPlugin() {
		super(ID);
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		inst = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		inst = null;
	}

}
