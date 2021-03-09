package io.jitclipse.ui.views.locks;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.lock.IOptimisedLock;
import io.jitclipse.core.model.lock.IOptimisedLockList;
import io.jitclipse.ui.views.AbstractHotspotView;

public class OptimizedLocksView extends AbstractHotspotView<IOptimisedLock> {

	public static final String ID = "io.jitclipse.ui.views.OptimizedLockView";

	@Override
	protected StructuredViewer createViewer(Composite parent) {
		return new OptimizedLocksViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
	}

	@Override
	protected Object toHotspotViewElement(Object viewerElement) {
		IOptimisedLock optimisedLock = (IOptimisedLock) viewerElement;
		return optimisedLock.getMethod();
	}

	@Override
	protected String getHelpId() {
		return "io.jitclipse.ui.help.optimizedLock.viewer";
	}

	protected void doubleClicked(IOptimisedLock optimisedLock) {
		showMessage("Double-click detected on " + optimisedLock);
	}

	private void showMessage(String message) {
		StructuredViewer viewer = getViewer();
		MessageDialog.openInformation(viewer.getControl().getShell(), "Optimized Lock View", message);
	}

	@Override
	protected void updateViewer(Viewer viewer, IHotspotLog hotspotLog) {
		IOptimisedLockList optimisedLockList = null;

		if (hotspotLog != null) {
			optimisedLockList = hotspotLog.getOptimizedLockList();
		}

		viewer.setInput(optimisedLockList);
	}

}
