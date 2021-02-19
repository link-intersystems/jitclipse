package io.jitclipse.ui.navigator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class HotspotLogFolderFilter extends ViewerFilter {

	public HotspotLogFolderFilter() {
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return true;
	}

}
