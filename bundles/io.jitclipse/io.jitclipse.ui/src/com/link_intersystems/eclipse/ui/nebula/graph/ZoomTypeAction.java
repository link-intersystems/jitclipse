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
