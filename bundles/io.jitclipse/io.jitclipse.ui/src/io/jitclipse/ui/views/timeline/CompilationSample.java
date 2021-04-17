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

import org.eclipse.nebula.visualization.xygraph.dataprovider.ISample;

import io.jitclipse.core.model.ICompilation;

class CompilationSample implements ISample {

	private ICompilation compilation;

	public CompilationSample(ICompilation compilation) {
		this.compilation = compilation;
	}

	@Override
	public double getXValue() {
		return compilation.getNMethodEmittedTime();
	}

	@Override
	public double getYValue() {
		return compilation.getId();
	}

	@Override
	public double getXPlusError() {
		return 0;
	}

	@Override
	public double getYPlusError() {
		return 0;
	}

	@Override
	public double getXMinusError() {
		return 0;
	}

	@Override
	public double getYMinusError() {
		return 0;
	}

	@Override
	public String getInfo() {
		return "";
	}

}