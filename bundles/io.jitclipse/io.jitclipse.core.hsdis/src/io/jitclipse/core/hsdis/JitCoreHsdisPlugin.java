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
package io.jitclipse.core.hsdis;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.osgi.framework.BundleContext;

import com.link_intersystems.eclipse.core.runtime.runtime.DefaultPlugin;

public class JitCoreHsdisPlugin extends DefaultPlugin {

	public static final String ID = "io.jitclipse.core.hsdis";

	private static JitCoreHsdisPlugin inst;

	public static JitCoreHsdisPlugin getInstance() {
		return inst;
	}

	public JitCoreHsdisPlugin() {
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

	public File getLibraryFolder() {
		IPath stateLocation = getStateLocation();

		IPath libraryPath = stateLocation.append("hsdis");
		File libraryFolder = new File(libraryPath.toOSString());

		if(!libraryFolder.exists()) {
			libraryFolder.mkdirs();
		}

		return libraryFolder;

	}

}
