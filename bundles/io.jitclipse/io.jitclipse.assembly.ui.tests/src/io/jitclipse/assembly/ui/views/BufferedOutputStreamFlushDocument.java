package io.jitclipse.assembly.ui.views;

import java.io.IOException;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

class BufferedOutputStreamFlushDocument extends AssemblyDocument {

	public static final TokenRange MOV_INSTR = new TokenRange(155, 158);
	public static final TokenRange SIMPLE_INSTR_LINE = new TokenRange(135, 174);
	public static final TokenRange MULTI_INSTR_LINE = new TokenRange(135, 220);
	public static final TokenRange COMMENT_LINE = new TokenRange(327, 402);
	public static final TokenRange BLOCK_LINE = new TokenRange(120, 133);

	public BufferedOutputStreamFlushDocument() throws IOException {
		super("BufferedOutputStream.flush.txt");
	}

	public void configureScanner(IPartitionTokenScanner scanner) {
		IDocument document = getDocument();
		scanner.setRange(document, 0, document.getLength());
	}

	public void configureScanner(IPartitionTokenScanner scanner, TokenRange tokenRange) {
		IDocument document = getDocument();
		scanner.setRange(document, tokenRange.getStart(), tokenRange.getEnd());
	}

}
