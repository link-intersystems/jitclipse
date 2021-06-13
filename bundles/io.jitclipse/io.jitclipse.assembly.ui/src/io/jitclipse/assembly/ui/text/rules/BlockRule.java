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

import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordPatternRule;

public class BlockRule extends WordPatternRule {

	public final static String BLOCK = "__assembly_block";

	public BlockRule() {
		super(new IWordDetector() {

			@Override
			public boolean isWordStart(char c) {
				return Character.isAlphabetic(c);
			}

			@Override
			public boolean isWordPart(char c) {
				return c == ']' || Character.isAlphabetic(c) || c == ' ';
			}
		}, "[", "]", new Token(BLOCK));
	}
}
