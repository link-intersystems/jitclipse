package io.jitclipse.ui.views.allocations;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.link_intersystems.eclipse.ui.jface.viewers.AdaptableSelectionList;

import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.allocation.IEliminatedAllocation;
import io.jitclipse.core.model.allocation.IEliminatedAllocationList;
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

	@Override
	protected String selectionToString(ISelection selection) {
		List<IEliminatedAllocation> eliminatedAllocations = new AdaptableSelectionList<>(IEliminatedAllocation.class,
				selection);
		return toString(eliminatedAllocations);
	}

	private String toString(List<IEliminatedAllocation> eliminatedAllocations) {
		if (eliminatedAllocations.isEmpty()) {
			return "";
		}

		IEliminatedAllocation eliminatedAllocation = eliminatedAllocations.get(0);

		return eliminatedAllocation.getMethod().getType().getName();
	}

	@Override
	protected IMethod toMethod(IEliminatedAllocation element) {
		return element.getMethod();
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
