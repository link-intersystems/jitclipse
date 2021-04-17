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

import org.adoptopenjdk.jitwatch.report.Report;

import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.suggestion.ISuggestion;
import io.jitclipse.jitwatch.core.model.ModelContext;

public class ReportSuggestion implements ISuggestion {

	private Report report;
	private ModelContext modelContext;

	public ReportSuggestion(Report suggestionReport, ModelContext modelContext) {
		this.report = suggestionReport;
		this.modelContext = modelContext;
	}

	@Override
	public String getText() {
		return report.getText();
	}

	@Override
	public int getScore() {
		return report.getScore();
	}

	@Override
	public String getType() {
		return report.getType().name();
	}

	@Override
	public IMethod getMethod() {
		return modelContext.getMethod(report.getCaller());
	}

}
