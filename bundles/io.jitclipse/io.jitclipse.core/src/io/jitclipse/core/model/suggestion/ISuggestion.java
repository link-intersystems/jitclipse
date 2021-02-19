package io.jitclipse.core.model.suggestion;

import io.jitclipse.core.model.IMethod;

public interface ISuggestion {

	int getScore();

	String getText();

	String getType();

	public IMethod getMethod();

}
