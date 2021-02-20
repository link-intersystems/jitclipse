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
