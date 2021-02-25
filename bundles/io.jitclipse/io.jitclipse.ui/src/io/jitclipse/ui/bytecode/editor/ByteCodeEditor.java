package io.jitclipse.ui.bytecode.editor;

import org.eclipse.ui.editors.text.TextEditor;

public class ByteCodeEditor extends TextEditor {

	public static final String ID = "io.jitclipse.ui.bytecode.editor";

	private ColorManager colorManager;

	public ByteCodeEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new ViewerConfiguration(colorManager));
		setDocumentProvider(new ByteCodeDocumentProvider());
	}

	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
