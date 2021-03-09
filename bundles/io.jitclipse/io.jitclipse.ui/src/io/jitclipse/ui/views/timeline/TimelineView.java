package io.jitclipse.ui.views.timeline;

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.visualization.xygraph.dataprovider.IMetaData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.link_intersystems.eclipse.ui.nebula.graph.XYGraphViewer;

import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.ICompilationList;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.JitCompiler;
import io.jitclipse.core.model.lock.IOptimisedLock;
import io.jitclipse.ui.views.AbstractHotspotView;
import io.jitclipse.ui.views.JitSelectionChange;

public class TimelineView extends AbstractHotspotView<IOptimisedLock> {

	public static final String ID = "io.jitclipse.ui.views.TimelineView";

	@Override
	protected StructuredViewer createViewer(Composite parent) {
		TimelineViewer timelineViewer = new TimelineViewer(parent, SWT.NONE);
		timelineViewer.setToolTipProvider(this::getToolTipText);
		return timelineViewer;
	}

	private String getToolTipText(Object element) {
		if (element instanceof IMetaData) {
			IMetaData sample = (IMetaData) element;
			ICompilation compilation = (ICompilation) sample.getData();
			IMethod method = compilation.getMethod();
			JitCompiler compiler = compilation.getCompiler();
			return method.getName() + " [" + compiler.name() + "]";
		}

		return null;

	}

	@Override
	protected TimelineViewer getViewer() {
		return (TimelineViewer) super.getViewer();
	}

	@Override
	protected String getHelpId() {
		return "io.jitclipse.ui.help.timeline.viewer";
	}

	protected void doubleClicked(IOptimisedLock optimisedLock) {
		showMessage("Double-click detected on " + optimisedLock);
	}

	private void showMessage(String message) {
		StructuredViewer viewer = getViewer();
		MessageDialog.openInformation(viewer.getControl().getShell(), "Optimized Lock View", message);
	}

	@Override
	protected void onSelectionChanged(JitSelectionChange jitSelectionChange) {
		super.onSelectionChanged(jitSelectionChange);

		jitSelectionChange.onMethodChanged(this::onMethodChanged);
	}

	private void onMethodChanged(IMethod method) {
		TimelineViewer viewer = getViewer();
		viewer.setHighlightMethod(method);
	}

	@Override
	protected void updateViewer(Viewer viewer, IHotspotLog hotspotLog) {
		ICompilationList compilationList = null;

		if (hotspotLog != null) {
			compilationList = hotspotLog.getCompilationList();
		}

		getViewer().setInput(compilationList);
	}

	protected void fillLocalPullDown(IContributionManager manager) {
		XYGraphViewer xyGraphViewer = getViewer();
		manager.add(xyGraphViewer.getZoomHorizontalAction());
		manager.add(xyGraphViewer.getZoomVerticalAction());
		manager.add(xyGraphViewer.getZoomNoneAction());
		manager.add(xyGraphViewer.getZoomPanningAction());
		manager.add(xyGraphViewer.getZoomRubberbandAction());
		manager.add(new Separator());
		manager.add(xyGraphViewer.getSaveSnapshotAction());
	}

	protected void fillContextMenu(IContributionManager manager) {
		fillLocalToolBar(manager);
		manager.add(new Separator());
		fillLocalPullDown(manager);
	}

	protected void fillLocalToolBar(IContributionManager manager) {
		XYGraphViewer xyGraphViewer = getViewer();
		manager.add(xyGraphViewer.getAutoScaleAction());
		manager.add(xyGraphViewer.getZoomInAction());
		manager.add(xyGraphViewer.getZoomOutAction());
	}

}
