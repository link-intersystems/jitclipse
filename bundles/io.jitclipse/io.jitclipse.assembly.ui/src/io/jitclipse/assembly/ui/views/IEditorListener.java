package io.jitclipse.assembly.ui.views;

import org.eclipse.ui.IEditorReference;

public interface IEditorListener {

	default void editorActivated(IEditorReference editorReference) {
	}

	default void editorDeactivated(IEditorReference editorReference) {
	}

	default void editorChanged(IEditorReference editorReference) {
	}

}
