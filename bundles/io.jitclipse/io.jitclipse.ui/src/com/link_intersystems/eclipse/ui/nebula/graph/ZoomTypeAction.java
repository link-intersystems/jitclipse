package com.link_intersystems.eclipse.ui.nebula.graph;

import java.util.function.Supplier;

import org.eclipse.nebula.visualization.xygraph.figures.IXYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.ZoomType;

public class ZoomTypeAction extends AbstractXYGraphAction {

	private ZoomType zoomType;

	public ZoomTypeAction(Supplier<IXYGraph> graphSupplier, ZoomType zoomType) {
		this(zoomType.name(), graphSupplier, zoomType);
	}

	public ZoomTypeAction(String text, Supplier<IXYGraph> graphSupplier, ZoomType zoomType) {
		super(graphSupplier, text, new ImageImageDescriptor(zoomType.getIconImage()));
		setToolTipText(zoomType.getDescription());
		this.zoomType = zoomType;
	}

	protected void run(IXYGraph xyGraph) {
		xyGraph.setZoomType(zoomType);
	}

}
