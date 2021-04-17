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

import org.adoptopenjdk.jitwatch.report.Report;
import org.adoptopenjdk.jitwatch.report.ReportType;

import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.lock.IOptimisedLock;
import io.jitclipse.jitwatch.core.model.ModelContext;

public class ReportOptimizedLock implements IOptimisedLock {

	private Report optimizedLockReport;
	private ModelContext modelContext;

	public ReportOptimizedLock(Report optimizedLockReport, ModelContext modelContext) {
		this.modelContext = modelContext;
		this.optimizedLockReport = optimizedLockReport;
	}

	@Override
	public IMethod getMethod() {
		return modelContext.getMethod(optimizedLockReport.getCaller());
	}

	@Override
	public String getHow() {
		ReportType type = optimizedLockReport.getType();
		return type.name();
	}

	@Override
	public String getKind() {
		return optimizedLockReport.getText();
	}

	@Override
	public ICompilation getCompilation() {
		IMethod method = getMethod();
		List<ICompilation> compilations = method.getCompilations();
		int compilationIndex = optimizedLockReport.getCompilationIndex();
		ICompilation compilation = compilations.get(compilationIndex);
		return compilation;
	}

}
