package io.jitclipse.ui.bytecode.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import io.jitclipse.core.model.IByteCodeInstruction;
import io.jitclipse.core.model.IClassByteCode;
import io.jitclipse.core.model.IMemberByteCode;

public class ByteCodeDocumentProvider extends FileDocumentProvider {

	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(new ByteCodePartitionScanner(),
					new String[] { ByteCodePartitionScanner.INSTRUCTION, ByteCodePartitionScanner.COMMENT });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}

	@Override
	protected boolean setDocumentContent(IDocument document, IEditorInput editorInput, String encoding)
			throws CoreException {

		BytecodeEditorInput bytecodeEditorInput = (BytecodeEditorInput) editorInput;
		IClassByteCode classByteCode = bytecodeEditorInput.getAdapter(IClassByteCode.class);
		writeDocument(document, classByteCode);

		return true;
	}

	private void writeDocument(IDocument document, IClassByteCode classByteCode) {
		List<String> lines = new ArrayList<>();

		for (IMemberByteCode memberByteCode : classByteCode) {
			for (IByteCodeInstruction byteCodeInstruction : memberByteCode) {
				lines.add(byteCodeInstruction.toString());
			}
		}

		String text = String.join("\n", lines);

		document.set(text);
	}
}