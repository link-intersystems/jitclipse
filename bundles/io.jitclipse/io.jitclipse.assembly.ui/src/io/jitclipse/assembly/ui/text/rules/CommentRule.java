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

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import com.link_intersystems.eclipse.ui.jface.text.rules.CharacterScannerUtil;
import com.link_intersystems.eclipse.ui.jface.text.rules.IResetableCharacterScanner;
import com.link_intersystems.eclipse.ui.jface.text.rules.SimplePredicateRule;

public class CommentRule extends SimplePredicateRule {

	public CommentRule() {
		super(new Token(COMMENT));
	}

	private static final char COMMENT_START_CHAR = ';';
	public final static String COMMENT = "__assembly_comment";

	@Override
	protected IToken doEvaluate(IResetableCharacterScanner scanner, boolean resume) {
		int c = scanner.markAndRead();

		if(c == COMMENT_START_CHAR) {
			CharacterScannerUtil.scanUntilEol(scanner);
			return getSuccessToken();
		}

		return Token.UNDEFINED;
	}


}
