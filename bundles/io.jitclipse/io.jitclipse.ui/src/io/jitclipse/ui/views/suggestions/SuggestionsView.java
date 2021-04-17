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
package io.jitclipse.ui.views.suggestions;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.link_intersystems.eclipse.ui.jface.viewers.AdaptableSelectionList;

import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.suggestion.ISuggestion;
import io.jitclipse.core.model.suggestion.ISuggestionList;
import io.jitclipse.ui.views.AbstractHotspotView;

public class SuggestionsView extends AbstractHotspotView<ISuggestion> {

	public static final String ID = "io.jitclipse.ui.views.SuggestionsView";

	@Override
	protected StructuredViewer createViewer(Composite parent) {
		return new SuggestionsViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
	}

	@Override
	protected Object toHotspotViewElement(Object viewerElement) {
		ISuggestion suggestion = (ISuggestion) viewerElement;
		return suggestion.getMethod();
	}

	@Override
	protected String selectionToString(ISelection selection) {
		List<ISuggestion> suggestions = new AdaptableSelectionList<>(ISuggestion.class, selection);
		return toString(suggestions);
	}

	private String toString(List<ISuggestion> suggestions) {
		if (suggestions.isEmpty()) {
			return "";
		}

		ISuggestion suggestion = suggestions.get(0);

		return suggestion.getMethod().getType().getName();
	}

	@Override
	protected IMethod toMethod(ISuggestion element) {
		return element.getMethod();
	}

	@Override
	protected String getHelpId() {
		return "io.jitclipse.ui.viewer";
	}

	@Override
	protected void updateViewer(Viewer viewer, IHotspotLog hotspotLog) {
		ISuggestionList suggestionList = null;

		if (hotspotLog != null) {
			suggestionList = hotspotLog.getSuggestionList();
		}

		viewer.setInput(suggestionList);
	}

}
