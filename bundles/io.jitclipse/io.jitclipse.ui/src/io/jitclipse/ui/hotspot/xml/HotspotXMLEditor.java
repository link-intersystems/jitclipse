package io.jitclipse.ui.hotspot.xml;

import org.eclipse.ui.editors.text.TextEditor;

public class HotspotXMLEditor extends TextEditor {

	private ColorManager colorManager;

	public HotspotXMLEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
