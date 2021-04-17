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

import static org.eclipse.nebula.visualization.xygraph.linearscale.LinearScale.Orientation.HORIZONTAL;
import static org.eclipse.nebula.visualization.xygraph.linearscale.LinearScale.Orientation.VERTICAL;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.nebula.visualization.xygraph.figures.Axis;
import org.eclipse.nebula.visualization.xygraph.figures.DAxis;
import org.eclipse.nebula.visualization.xygraph.figures.IAxesFactory;

class LabelProviderAwareAxisFactory implements IAxesFactory {

	private ILabelProvider xAxisLabelProvider;
	private ILabelProvider yAxisLabelProvider;

	public void setxAxisLabelProvider(ILabelProvider xAxisLabelProvider) {
		this.xAxisLabelProvider = xAxisLabelProvider;
	}

	public void setyAxisLabelProvider(ILabelProvider yAxisLabelProvider) {
		this.yAxisLabelProvider = yAxisLabelProvider;
	}

	@Override
	public Axis createXAxis() {
		DAxis dAxis = new DAxis() {

			@Override
			public String format(Object obj, int extraDP) {
				String text;
				if (xAxisLabelProvider == null) {
					text = super.format(obj, extraDP);
				} else {
					text = xAxisLabelProvider.getText(obj);
				}

				if (text == null) {
					text = "";
				}

				return text;
			}
		};
		dAxis.setHasUserDefinedFormat(true);
		dAxis.setOrientation(HORIZONTAL);
		return dAxis;
	}

	@Override
	public Axis createYAxis() {
		DAxis dAxis = new DAxis() {

			@Override
			public String format(Object obj, int extraDP) {
				String text;
				if (yAxisLabelProvider == null) {
					text = super.format(obj, extraDP);
				} else {
					text = yAxisLabelProvider.getText(obj);
				}

				if (text == null) {
					text = "";
				}

				return text;
			}
		};
		dAxis.setHasUserDefinedFormat(true);
		dAxis.setOrientation(VERTICAL);
		return dAxis;
	}

}
