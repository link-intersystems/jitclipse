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
