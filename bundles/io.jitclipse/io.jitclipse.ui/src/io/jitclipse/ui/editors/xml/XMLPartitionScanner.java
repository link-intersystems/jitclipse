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
