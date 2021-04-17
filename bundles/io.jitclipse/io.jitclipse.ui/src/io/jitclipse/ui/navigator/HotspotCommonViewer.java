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

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonViewer;

import io.jitclipse.core.resources.IHotspotLogFile;

public class HotspotCommonViewer extends CommonViewer {

	private HotspotViewerController controller = new HotspotViewerController(this);

	public HotspotCommonViewer(String aViewerId, Composite aParent, int aStyle) {
		super(aViewerId, aParent, aStyle);
	}

	@Override
	protected void handleTreeExpand(TreeEvent event) {
		Object element = event.item.getData();
		if (IFile.class.isInstance(element)) {
			IFile file = IFile.class.cast(element);
			IHotspotLogFile hotspotLogFile = file.getAdapter(IHotspotLogFile.class);
			if (hotspotLogFile != null) {
				controller.openFile(hotspotLogFile);
			}
		}
		super.handleTreeExpand(event);
	}

}
