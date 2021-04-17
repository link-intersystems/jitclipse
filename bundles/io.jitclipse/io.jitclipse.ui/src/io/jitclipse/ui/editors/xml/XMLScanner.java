/*******************************************************************************
 * Copyright (c) 2021 Link Intersystems GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Link Intersystems GmbH - René Link - API and implementation
 *******************************************************************************/
package io.jitclipse.ui.editors.xml;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class XMLScanner extends BufferedRuleBasedScanner {

	public XMLScanner() {
		setBufferSize(1024);
	}

	public XMLScanner(ColorManager manager) {
		IToken procInstr = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.PROC_INSTR)));

		IRule[] rules = new IRule[2];
		// Add rule for processing instructions
		rules[0] = new SingleLineRule("<?", "?>", procInstr);
		// Add generic whitespace rule.
		rules[1] = new WhitespaceRule(new XMLWhitespaceDetector());

		setRules(rules);
	}
}
