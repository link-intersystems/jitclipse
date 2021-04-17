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

import org.eclipse.jface.viewers.IStructuredContentProvider;

public interface IGraphContentProvider extends IStructuredContentProvider {

	public String getXAxisTitle(Object inputElement);
	public String getYAxisTitle(Object inputElement);
	public String getTraceTitle(Object inputElement);
}
