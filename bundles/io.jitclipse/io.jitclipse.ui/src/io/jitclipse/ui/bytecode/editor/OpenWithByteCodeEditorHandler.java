package io.jitclipse.ui.bytecode.editor;

import java.util.Optional;

import org.eclipse.core.commands.ExecutionException;

import com.link_intersystems.eclipse.ui.commands.AbstractUIHandler;
import com.link_intersystems.eclipse.ui.commands.UIExecutionContext;
import com.link_intersystems.eclipse.ui.editors.IEditorOpener;
import com.link_intersystems.eclipse.ui.jface.viewers.SelectionList;

import io.jitclipse.core.model.IClass;

public class OpenWithByteCodeEditorHandler extends AbstractUIHandler {

	@Override
	protected Object execute(UIExecutionContext context) throws ExecutionException {
		SelectionList<IClass> classSelection = context.getSelection(IClass.class);
		Optional<IClass> firstElement = classSelection.getFirstElement();
		IEditorOpener<BytecodeEditorInput> editorOpener = context.getEditorOpener(BytecodeEditorInput.class,
				ByteCodeEditor.ID);

		firstElement.ifPresent(c -> editorOpener.openEditor(new BytecodeEditorInput(c.getByteCode())));
		return null;
	}

}
