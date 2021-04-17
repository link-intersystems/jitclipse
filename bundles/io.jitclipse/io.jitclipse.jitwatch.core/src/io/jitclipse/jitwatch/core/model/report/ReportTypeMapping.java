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
package io.jitclipse.jitwatch.core.model.report;

import java.util.HashMap;
import java.util.Map;

import org.adoptopenjdk.jitwatch.report.ReportType;

import io.jitclipse.core.model.JitOptimizationStrategy;

public class ReportTypeMapping {

	public static final ReportTypeMapping INSTANCE = new ReportTypeMapping();

	private Map<ReportType, JitOptimizationStrategy> optimizationStrategy = new HashMap<>();

	public ReportTypeMapping() {
		optimizationStrategy.put(ReportType.ELIMINATED_ALLOCATION_INLINE, JitOptimizationStrategy.INLINE);
		optimizationStrategy.put(ReportType.ELIMINATED_ALLOCATION_DIRECT, JitOptimizationStrategy.DIRECT);

		optimizationStrategy.put(ReportType.INLINE_FAILURE, JitOptimizationStrategy.INLINE);
		optimizationStrategy.put(ReportType.INLINE_SUCCESS, JitOptimizationStrategy.INLINE);

		optimizationStrategy.put(ReportType.BRANCH, JitOptimizationStrategy.BRANCH);
		optimizationStrategy.put(ReportType.CODE_CACHE, JitOptimizationStrategy.CODE_CACHE);
		optimizationStrategy.put(ReportType.HOT_THROW, JitOptimizationStrategy.HOT_THROW);
	}

	public JitOptimizationStrategy getStrategy(ReportType type) {
		return optimizationStrategy.get(type);
	}
}
