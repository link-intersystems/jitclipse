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
		commonViewer.expandToLevel(hotspotLogFile.getFile(), CommonViewer.ALL_LEVELS);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
