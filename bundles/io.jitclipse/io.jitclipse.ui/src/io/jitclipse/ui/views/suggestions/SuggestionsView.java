package io.jitclipse.ui.views.suggestions;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import io.jitclipse.core.model.IHotspotLog;
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
