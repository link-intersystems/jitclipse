package io.jitclipse.ui.editors.xml;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

public class TagRule extends MultiLineRule {

	public final static String XML_TAG = "__xml_tag";

	public TagRule() {
		super("<", ">", new Token(XML_TAG));
	}

	@Override
	protected boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed) {
		int c = scanner.read();
		if (sequence[0] == '<') {
			if (c == '?') {
				// processing instruction - abort
				scanner.unread();
				return false;
			}
			if (c == '!') {
				scanner.unread();
				// comment - abort
				return false;
			}
		} else if (sequence[0] == '>') {
			scanner.unread();
		}
		return super.sequenceDetected(scanner, sequence, eofAllowed);
	}
}
