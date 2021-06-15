package io.jitclipse.core.model.internal;

import org.eclipse.core.runtime.IAdapterFactory;

import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.suggestion.ISuggestion;

public class SuggestionAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, java.lang.Class<T> adapterType) {
		if (ISuggestion.class.isInstance(adaptableObject)) {
			ISuggestion suggestion = ISuggestion.class.cast(adaptableObject);
			return getAdapter(suggestion, adapterType);
		}
		return null;
	}

	public <T> T getAdapter(ISuggestion suggestion, java.lang.Class<T> adapterType) {
		if (IMethod.class.isAssignableFrom(adapterType)) {
			return adapterType.cast(suggestion.getMethod());
		}
		return null;
	}

	@Override
	public java.lang.Class<?>[] getAdapterList() {
		return new java.lang.Class<?>[] { IMethod.class };
	}

}
