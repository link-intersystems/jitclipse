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
