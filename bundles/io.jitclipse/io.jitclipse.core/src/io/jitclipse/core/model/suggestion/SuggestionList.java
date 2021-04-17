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
