package io.jitclipse.assembly.ui.views;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;

public class DocumentScan {

	private ITokenScanner scanner;
	private IDocument document;
	private IToken latestToken;

	public DocumentScan(ITokenScanner scanner, IDocument document) {
		this.scanner = scanner;
		this.document = document;
	}

	public String getTokenString() throws BadLocationException {
		int tokenOffset = scanner.getTokenOffset();
		int tokenLength = scanner.getTokenLength();

		if(tokenLength < 0) {
			return null;
		}

		try {
			String tokenString = document.get(tokenOffset, tokenLength);
			return tokenString;
		} catch (BadLocationException e) {
			throw e;
		}
	}

	public IToken nextToken() {
		latestToken = scanner.nextToken();
		return latestToken;
	}

	public IToken getLatestToken() {
		return latestToken;
	}

}
