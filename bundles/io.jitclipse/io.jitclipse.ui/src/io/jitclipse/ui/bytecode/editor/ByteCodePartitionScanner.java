package io.jitclipse.ui.bytecode.editor;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class ByteCodePartitionScanner extends RuleBasedPartitionScanner {
	public final static String COMMENT = "__xml_comment";
	public final static String INSTRUCTION = "__xml_tag";

	public ByteCodePartitionScanner() {

		IToken xmlComment = new Token(COMMENT);
		IToken tag = new Token(INSTRUCTION);

		IPredicateRule[] rules = new IPredicateRule[2];

		rules[0] = new MultiLineRule("<!--", "-->", xmlComment);
		rules[1] = new InstructionRule(tag);

		setPredicateRules(rules);
	}
}
