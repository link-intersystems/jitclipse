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

import com.link_intersystems.eclipse.ui.swt.widgets.Display2;

import io.jitclipse.core.resources.IHotspotLogFile;

public class HotspotViewerController {

	private HotspotCommonViewer hotspotCommonViewer;

	public HotspotViewerController(HotspotCommonViewer hotspotCommonViewer) {
		this.hotspotCommonViewer = hotspotCommonViewer;
	}

	public void openFile(IHotspotLogFile hotspotLogFile) {
		if (!hotspotLogFile.isOpened() && !hotspotLogFile.isLoading()) {
			hotspotLogFile.open(Display2.syncAdapter((IHotspotLogFile hlf) -> {
				hotspotCommonViewer.refresh(hlf.getFile());
			}), null);
		}
	}

}
