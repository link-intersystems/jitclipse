package io.jitclipse.ui.views.allocations;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.allocation.IEliminatedAllocation;
import io.jitclipse.core.model.allocation.IEliminatedAllocationList;
import io.jitclipse.core.model.suggestion.ISuggestion;
import io.jitclipse.ui.views.AbstractHotspotView;

public class EliminatedAllocationsView extends AbstractHotspotView<IEliminatedAllocation> {

	public static final String ID = "io.jitclipse.ui.views.EliminatedAllocationView";

	@Override
	protected StructuredViewer createViewer(Composite parent) {
		return new EliminatedAllocationsViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
	}

	@Override
	protected Object toHotspotViewElement(Object viewerElement) {
		IEliminatedAllocation eliminatedAllocation = (IEliminatedAllocation) viewerElement;
		return eliminatedAllocation.getMethod();
	}

	@Override
	protected String getHelpId() {
		return "io.jitclipse.ui.help.eliminatedAllocation.viewer";
	}

	protected void doubleClicked(ISuggestion suggestion) {
		showMessage("Double-click detected on " + suggestion);
	}

	private void showMessage(String message) {
		StructuredViewer viewer = getViewer();
		MessageDialog.openInformation(viewer.getControl().getShell(), "Suggestions View", message);
	}

	@Override
	protected void updateViewer(Viewer viewer, IHotspotLog hotspotLog) {
		IEliminatedAllocationList eliminatedAllocationList = null;

		if (hotspotLog != null) {
			eliminatedAllocationList = hotspotLog.getEliminatedAllocationList();
		}

		viewer.setInput(eliminatedAllocationList);
	}

}
