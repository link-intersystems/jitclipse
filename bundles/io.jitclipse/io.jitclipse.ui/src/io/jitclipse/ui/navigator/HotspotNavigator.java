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
package io.jitclipse.ui.navigator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

import io.jitclipse.core.resources.IHotspotLogFile;

public class HotspotNavigator extends CommonNavigator {

	public static final String ID = "io.jitclipse.ui.hotspotNavigator";

	protected CommonViewer createCommonViewerObject(Composite aParent) {
		return new HotspotCommonViewer(getViewSite().getId(), aParent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
	}

	@Override
	protected CommonViewer createCommonViewer(Composite aParent) {
		CommonViewer commonViewer = super.createCommonViewer(aParent);
		return commonViewer;
	}

	public void expandHotspotLogFile(IHotspotLogFile hotspotLogFile) {
		CommonViewer commonViewer = getCommonViewer();
		commonViewer.expandToLevel(hotspotLogFile.getFile(), 1);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
