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
package io.jitclipse.jitwatch.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

import com.link_intersystems.eclipse.ui.jface.plugin.AbstractJFacePlugin;

public class JitWatchUIPlugin extends AbstractJFacePlugin implements IJitUIPluginContext {

	public static final String ID = "io.jitclipse.jitwatch.ui";

	private static JitWatchUIPlugin inst;

	private BundleContext context;

	public static JitWatchUIPlugin getInstance() {
		return inst;
	}

	public JitWatchUIPlugin() {
		super(ID);
	}

	public static IStatus errorStatus(String message, Throwable t) {
		return new Status(IStatus.ERROR, ID, IStatus.ERROR, message, t);
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		inst = this;
		context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		inst = null;
		context = null;
	}

	public BundleContext getContext() {
		return context;
	}

	public JitWatchUIImages getJitUIImages() {
		return getImageResourcesProxy(JitWatchUIImages.class);
	}

}
