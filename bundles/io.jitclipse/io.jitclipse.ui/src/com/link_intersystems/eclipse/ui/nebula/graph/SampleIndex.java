package com.link_intersystems.eclipse.ui.nebula.graph;

import org.eclipse.nebula.visualization.xygraph.dataprovider.IDataProvider;
import org.eclipse.nebula.visualization.xygraph.dataprovider.IDataProviderListener;
import org.eclipse.nebula.visualization.xygraph.dataprovider.ISample;

public class SampleIndex {

	private IDataProviderListener dataProviderAdapter = new IDataProviderListener() {

		@Override
		public void dataChanged(IDataProvider dataProvider) {
			clearIndex();
		}
	};

	private IDataProvider dataProvider;

	public IDataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(IDataProvider dataProvider) {
		if (this.dataProvider != null) {
			this.dataProvider.removeDataProviderListener(dataProviderAdapter);
		}

		this.dataProvider = dataProvider;

		if (this.dataProvider != null) {
			this.dataProvider.addDataProviderListener(dataProviderAdapter);
		}
	}

	public ISample getSampleAt(double xValue, double yValue) {
		return getSampleAt(xValue, yValue, 0.1, 0.1);
	}

	public ISample getSampleAt(double xValue, double yValue, double xDiff, double yDiff) {
		ISample sampleByXValue = null;

		if (dataProvider != null) {
			for (int i = 0; i < dataProvider.getSize(); i++) {
				ISample sample = dataProvider.getSample(i);
				double sampleXValue = sample.getXValue();

				if (isEqual(xValue, sampleXValue, xDiff)) {
					double sampleYValue = sample.getYValue();
					if (isEqual(yValue, sampleYValue, yDiff)) {
						sampleByXValue = sample;
						break;
					}
				}

			}
		}

		return sampleByXValue;
	}

	private boolean isEqual(double v1, double v2, double delta) {
		double diff = v1 - v2;
		return Math.abs(diff - 1.0) <= delta;
	}

	protected void clearIndex() {
	}
}
