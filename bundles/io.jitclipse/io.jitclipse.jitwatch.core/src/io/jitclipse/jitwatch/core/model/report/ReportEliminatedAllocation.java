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

import java.util.List;

import org.adoptopenjdk.jitwatch.model.IMetaMember;
import org.adoptopenjdk.jitwatch.model.MetaClass;
import org.adoptopenjdk.jitwatch.report.Report;
import org.adoptopenjdk.jitwatch.report.ReportType;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IOptimization;
import io.jitclipse.core.model.JitOptimizationStrategy;
import io.jitclipse.core.model.allocation.IEliminatedAllocation;
import io.jitclipse.jitwatch.core.model.ModelContext;
import io.jitclipse.jitwatch.core.model.Optimization;

public class ReportEliminatedAllocation implements IEliminatedAllocation {

	private ReportTypeMapping reportTypeMapping = ReportTypeMapping.INSTANCE;
	private Report eliminatedAllocationReport;
	private ModelContext modelContext;

	public ReportEliminatedAllocation(Report eliminatedAllocationReport, ModelContext modelContext) {
		this.eliminatedAllocationReport = eliminatedAllocationReport;
		this.modelContext = modelContext;
	}

	@Override
	public IMethod getMethod() {
		IMetaMember caller = eliminatedAllocationReport.getCaller();
		return modelContext.getMethod(caller);
	}

	@Override
	public ICompilation getCompilation() {
		IMethod method = getMethod();
		List<ICompilation> compilations = method.getCompilations();
		int compilationIndex = eliminatedAllocationReport.getCompilationIndex();
		ICompilation compilation = compilations.get(compilationIndex);
		return compilation;
	}

	@Override
	public IOptimization getOptimization() {
		ReportType type = eliminatedAllocationReport.getType();
		JitOptimizationStrategy strategy = reportTypeMapping.getStrategy(type);
		return new Optimization(strategy);
	}

	@Override
	public IClass getEliminatedType() {
		MetaClass eliminatedType = null;

		Object metaData = eliminatedAllocationReport.getMetaData();

		if (metaData instanceof MetaClass) {
			eliminatedType = ((MetaClass) metaData);
		}

		return modelContext.getClass(eliminatedType);
	}

}
