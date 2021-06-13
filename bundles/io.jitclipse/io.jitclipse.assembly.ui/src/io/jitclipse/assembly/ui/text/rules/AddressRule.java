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

import static org.eclipse.jface.text.rules.ICharacterScanner.EOF;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import com.link_intersystems.eclipse.ui.jface.text.rules.AbstractPredicateRule;

public class AddressRule extends AbstractPredicateRule {

	public final static String ADDRESS = "__assembly_address";
	public static final String ADDRESS_REF = "__assembly_address_ref";
	private Token addressRefToken;
	private Token addressToken;

	public AddressRule() {
		addressToken = new Token(ADDRESS);
		addressRefToken = new Token(ADDRESS_REF);
	}

	@Override
	public IToken getSuccessToken() {
		return addressToken;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		IToken token = Token.UNDEFINED;

		int column = scanner.getColumn();

		IToken success = column == 0 ? getSuccessToken() : addressRefToken;

		int c = scanner.read();

		if (c == '0') {
			c = scanner.read();
			if (c == 'x') {
				while ((c = scanner.read()) != EOF) {
					if (isHexPart((char) c)) {
						token = success;
					} else {
						scanner.unread();
						break;
					}
				}
			} else {
				scanner.unread();
				scanner.unread();
			}
		} else {
			scanner.unread();
		}

		return token;
	}

	private boolean isHexPart(char c) {
		return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F');
	}
}
