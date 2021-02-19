package com.link_intersystems.eclipse.ui.nebula.graph;

import java.util.function.Supplier;

import org.eclipse.nebula.visualization.xygraph.figures.IXYGraph;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;

public class AutoScaleAction extends AbstractXYGraphAction {

	public AutoScaleAction(Supplier<IXYGraph> graphSupplier) {
		super(graphSupplier, "Auto Scale",
				new ImageImageDescriptor(XYGraphMediaFactory.getInstance().getImage("images/AutoScale.png")));
		setToolTipText("Perform Auto Scale");
	}

	@Override
	protected void run(IXYGraph xyGraph) {
		xyGraph.performAutoScale();
	}

}
