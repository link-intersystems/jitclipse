package io.jitclipse.ui.navigator;

import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

import io.jitclipse.core.resources.IHotspotLogFile;

public class HotspotNavigator extends CommonNavigator {

	public static final String ID = "io.jitclipse.ui.hotspotNavigator";

	public void expandHotspotLogFile(IHotspotLogFile hotspotLogFile) {
		CommonViewer commonViewer = getCommonViewer();
		commonViewer.expandToLevel(hotspotLogFile.getFile(), CommonViewer.ALL_LEVELS);
	}

}
