package com.link_intersystems.eclipse.ui.nebula.graph;

import org.eclipse.jface.viewers.IStructuredContentProvider;

public interface IGraphContentProvider extends IStructuredContentProvider {

	public String getXAxisTitle(Object inputElement);
	public String getYAxisTitle(Object inputElement);
	public String getTraceTitle(Object inputElement);
}
