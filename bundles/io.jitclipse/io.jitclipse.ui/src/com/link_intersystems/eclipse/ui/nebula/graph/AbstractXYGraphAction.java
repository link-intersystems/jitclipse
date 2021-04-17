/*******************************************************************************
 * Copyright (c) 2021 Link Intersystems GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Link Intersystems GmbH - René Link - API and implementation
 *******************************************************************************/
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