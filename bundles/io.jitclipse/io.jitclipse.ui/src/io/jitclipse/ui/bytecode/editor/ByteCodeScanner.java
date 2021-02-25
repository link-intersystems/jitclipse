package io.jitclipse.ui.bytecode.editor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class ByteCodeScanner extends BufferedRuleBasedScanner {

	public ByteCodeScanner() {
		setBufferSize(1024);
	}

	public ByteCodeScanner(ColorManager manager) {
		IToken procInstr = new Token(new TextAttribute(manager.getColor(IColorConstants.DEFAULT)));

		IRule[] rules = new IRule[2];
		// Add rule for processing instructions
		rules[0] = new SingleLineRule("<?", "?>", procInstr);
		// Add generic whitespace rule.
		rules[1] = new WhitespaceRule(new WhitespaceDetector());

		setRules(rules);
	}
}
