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
package io.jitclipse.ui.views.timeline;

import java.util.Comparator;
import java.util.TreeSet;

import org.eclipse.nebula.visualization.xygraph.dataprovider.ISample;
import org.eclipse.nebula.visualization.xygraph.dataprovider.Sample;

import com.link_intersystems.eclipse.ui.nebula.graph.IGraphContentProvider;

import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.ICompilationList;

public class CompilationContentProvider implements IGraphContentProvider {

	@Override
	public String getTraceTitle(Object inputElement) {
		return null;
	}

	@Override
	public String getXAxisTitle(Object inputElement) {
		return "Time";
	}

	@Override
	public String getYAxisTitle(Object inputElement) {
		return "Event";
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ICompilationList) {
			ICompilationList compilationList = (ICompilationList) inputElement;
			return toSamples(compilationList);
		}
		return null;
	}

	private ISample[] toSamples(ICompilationList compilationList) {
		TreeSet<ISample> samples = new TreeSet<>(new Comparator<ISample>() {

			@Override
			public int compare(ISample o1, ISample o2) {
				double xValue1 = o1.getXValue();
				double xValue2 = o2.getXValue();
				return Double.compare(xValue1, xValue2);
			}
		});

		long index = 0;
		for (int i = 0; i < compilationList.size(); i++) {
			ICompilation compilation = compilationList.get(i);

			double xData = compilation.getStartTimestamp();
			double yData = index++;
			Sample queuedTimestampSample = new Sample(xData, yData);
			queuedTimestampSample.setData(compilation);
			samples.add(queuedTimestampSample);

		}

		return samples.toArray(new ISample[samples.size()]);
	}

}
