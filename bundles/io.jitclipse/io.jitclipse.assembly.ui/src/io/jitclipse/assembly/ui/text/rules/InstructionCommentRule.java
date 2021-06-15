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
package io.jitclipse.assembly.ui.text.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import com.link_intersystems.eclipse.ui.jface.text.rules.AbstractPredicateRule;
import com.link_intersystems.eclipse.ui.jface.text.rules.CharacterScannerUtil;

public class InstructionCommentRule extends AbstractPredicateRule {

	private static final char COMMENT_START_CHAR = ';';
	public final static String INSTRUCTION_COMMENT = "__assembly_instruction_comment";

	private Token comment = new Token(INSTRUCTION_COMMENT);

	public InstructionCommentRule() {
	}

	@Override
	public IToken getSuccessToken() {
		return comment;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		IToken token = Token.UNDEFINED;

		int c = scanner.read();

		if (c == COMMENT_START_CHAR) {
			CharacterScannerUtil.scanUntilEol(scanner);
			token = getSuccessToken();
		} else {
			scanner.unread();
		}

		return token;
	}

}
