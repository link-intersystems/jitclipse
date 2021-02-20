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
