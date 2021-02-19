package io.jitclipse.ui.views;

import java.util.Objects;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;

public abstract class LinkWithSelectionViewerFiler extends ViewerFilter {

	private class SelectionChangeAdapter implements ISelectionListener {

		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (viewId == null) {
				return;
			}
			if (viewId.equals(part.getSite().getId())) {
				setSelection(selection);
			}
		}

	}

	private SelectionChangeAdapter selectionAdapter = new SelectionChangeAdapter();

	private ISelectionService selectionService;
	private boolean enabled;
	private Viewer viewer;
	private ISelection selection;

	private IWorkbenchPartSite site;

	private String viewId;

	public LinkWithSelectionViewerFiler(Viewer viewer) {
		this.viewer = Objects.requireNonNull(viewer);
	}

	public void setViewIdToLinkWith(String viewId) {
		this.viewId = viewId;
	}

	public void setViewSite(IWorkbenchPartSite site) {
		setSelectionService(null);

		this.site = site;

		if (this.site != null) {
			ISelectionService selectionService = this.site.getWorkbenchWindow().getSelectionService();
			setSelectionService(selectionService);
		}
	}

	private void setSelection(ISelection selection) {
		ISelection oldSelection = this.selection;
		this.selection = selection;

		if (!Objects.equals(selection, oldSelection)) {
			onSelectionChanged(oldSelection, selection);
		}

		viewer.refresh();
	}

	protected void onSelectionChanged(ISelection oldSelection, ISelection newSelection) {
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		ISelection effectiveSelection;
		if (selection == null) {
			effectiveSelection = new StructuredSelection();
		} else {
			effectiveSelection = selection;
		}
		return select(viewer, parentElement, element, effectiveSelection);
	}

	protected abstract boolean select(Viewer viewer, Object parentElement, Object element, ISelection selection);

	private void setSelectionService(ISelectionService selectionService) {
		if (this.selectionService != null) {
			this.selectionService.removeSelectionListener(selectionAdapter);
		}

		this.selectionService = selectionService;

		if (this.selectionService != null) {
			if (enabled) {
				this.selectionService.addSelectionListener(selectionAdapter);
			}
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		if (this.selectionService != null) {
			if (enabled) {
				this.selectionService.addSelectionListener(selectionAdapter);
				ISelection selection = selectionService.getSelection(viewId);
				setSelection(selection);
			} else {
				this.selectionService.removeSelectionListener(selectionAdapter);
				setSelection(new StructuredSelection());
			}
		}
	}

}
