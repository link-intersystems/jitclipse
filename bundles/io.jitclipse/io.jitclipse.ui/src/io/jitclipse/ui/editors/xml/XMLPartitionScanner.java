package io.jitclipse.ui.editors.xml;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

public class XMLPartitionScanner extends RuleBasedPartitionScanner {


	public XMLPartitionScanner() {

		IPredicateRule[] rules = new IPredicateRule[2];

		rules[0] = new CommentRule();
		rules[1] = new TagRule();

		setPredicateRules(rules);
	}
}
