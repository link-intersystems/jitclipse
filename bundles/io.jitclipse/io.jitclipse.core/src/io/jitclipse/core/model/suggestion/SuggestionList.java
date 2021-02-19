package io.jitclipse.core.model.suggestion;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import io.jitclipse.core.model.IMethod;

public class SuggestionList extends AbstractList<ISuggestion> implements ISuggestionList {

	private List<ISuggestion> suggestions;

	public SuggestionList(List<ISuggestion> suggestionReports) {
		this.suggestions = suggestionReports;
	}

	@Override
	public List<ISuggestion> getTypeSuggestions(String fullyQualifiedName) {
		List<ISuggestion> typeSuggestions = new ArrayList<>();

		for (ISuggestion suggestion : suggestions) {
			IMethod method = suggestion.getMethod();
			if (method.matches(fullyQualifiedName)) {
				typeSuggestions.add(suggestion);
			}
		}

		return typeSuggestions;
	}

	@Override
	public ISuggestion get(int index) {
		return suggestions.get(index);
	}

	@Override
	public int size() {
		return suggestions.size();
	}
}
