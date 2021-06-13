package io.jitclipse.assembly.ui.views;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.junit.Assert;

public class ScannerTestDriver {

	private DocumentScan documentScan;

	public ScannerTestDriver(DocumentScan documentScan) {
		this.documentScan = documentScan;
	}

	public void expectNextToken(String expectedTokenString) {
		documentScan.nextToken();
		expectTokenString(expectedTokenString);
	}

	public void expectNextDefaultToken(String expectedTokenString) {
		expectNextToken(expectedTokenString, null);
	}

	public void expectNextToken(String expectedTokenString, Object expectedTokenData) {
		IToken nextToken = documentScan.nextToken();
		assertEquals(expectedTokenData, nextToken.getData(), "nextTokenData");
		expectTokenString(expectedTokenString);
	}

	private void expectTokenString(String expectedTokenString) {
		try {
			String actualTokenString = documentScan.getTokenString();
			assertEquals(expectedTokenString, actualTokenString, "token string");
		} catch (BadLocationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	public void expectNextTokenString(String expectedTokenString) {
		StringBuilder sb = new StringBuilder();
		IToken nextToken = null;

		while ((nextToken = documentScan.nextToken()) != null && !nextToken.isEOF()) {

			try {
				String tokenString = documentScan.getTokenString();
				sb.append(tokenString);

				if (sb.toString().startsWith(expectedTokenString)) {
					return;
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage());
			}
		}

		Assert.fail("next token string expected '" + expectedTokenString + "', but was '" + sb + "'");

	}

	public void expectNextDefaultToken() {
		IToken nextToken = documentScan.nextToken();
		assertEquals(null, nextToken.getData(), "nextTokenData");
	}

	public void exprectWhitespace() {
		expectNextToken(Token.WHITESPACE);
	}

	private void expectNextToken(IToken token) {
		IToken nextToken = documentScan.nextToken();
		assertEquals(token, nextToken);

	}

}
