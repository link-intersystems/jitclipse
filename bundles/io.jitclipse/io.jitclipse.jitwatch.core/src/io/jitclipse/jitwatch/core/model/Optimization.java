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
package io.jitclipse.jitwatch.core.model;

import io.jitclipse.core.model.IOptimization;
import io.jitclipse.core.model.JitOptimizationStrategy;

public class Optimization implements IOptimization {

	private JitOptimizationStrategy jitOptimizationStrategy;

	public Optimization(JitOptimizationStrategy jitOptimizationStrategy) {
		this.jitOptimizationStrategy = jitOptimizationStrategy;
	}

	@Override
	public JitOptimizationStrategy getStrategy() {
		return jitOptimizationStrategy;
	}

}
