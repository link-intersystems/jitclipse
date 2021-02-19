package com.link_intersystems.eclipse.ui.nebula.graph;

import java.util.function.Supplier;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.nebula.visualization.xygraph.figures.IXYGraph;

public abstract class AbstractXYGraphAction extends Action {

	protected Supplier<IXYGraph> graphSupplier;

	public AbstractXYGraphAction(Supplier<IXYGraph> graphSupplier) {
		this.graphSupplier = graphSupplier;
	}

	public AbstractXYGraphAction(Supplier<IXYGraph> graphSupplier, String text) {
		super(text);
		this.graphSupplier = graphSupplier;
	}

	public AbstractXYGraphAction(Supplier<IXYGraph> graphSupplier, String text, ImageDescriptor image) {
		super(text, image);
		this.graphSupplier = graphSupplier;
	}

	public AbstractXYGraphAction(Supplier<IXYGraph> graphSupplier, String text, int style) {
		super(text, style);
		this.graphSupplier = graphSupplier;
	}

	@Override
	public final void run() {
		IXYGraph xyGraph = graphSupplier.get();
		if(xyGraph != null) {
			run(xyGraph);
		}
	}

	protected abstract void run(IXYGraph xyGraph);

}