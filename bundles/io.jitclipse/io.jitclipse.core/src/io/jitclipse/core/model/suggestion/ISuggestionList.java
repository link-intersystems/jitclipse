package io.jitclipse.core.model.suggestion;

import java.util.List;

public interface ISuggestionList extends List<ISuggestion> {

	List<ISuggestion> getTypeSuggestions(String fullyQualifiedName);

}
