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

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.Token;

import com.link_intersystems.eclipse.ui.jface.text.rules.WordDetectionPredicateRule;


public class InstructionRule extends WordDetectionPredicateRule implements IPredicateRule {

	public final static String INSTRUCTION = "__assembly_instruction";

	public InstructionRule() {
		super(new Token(INSTRUCTION));
		addWord("add");
		addWord("addq");
		addWord("and");
		addWord("callq");
		addWord("cmove");
		addWord("cmp");
		addWord("cmpb");
		addWord("cmpl");
		addWord("cmpq");
		addWord("data16");
		addWord("dec");
		addWord("hlt");
		addWord("inc");
		addWord("incl");
		addWord("jae");
		addWord("jb");
		addWord("je");
		addWord("jg");
		addWord("jge");
		addWord("jl");
		addWord("jle");
		addWord("jmp");
		addWord("jmpq");
		addWord("jne");
		addWord("jno");
		addWord("lea");
		addWord("leaveq");
		addWord("lock");
		addWord("mov");
		addWord("movabs");
		addWord("movb");
		addWord("movl");
		addWord("movq");
		addWord("movsbl");
		addWord("movslq");
		addWord("movzbl");
		addWord("movzwl");
		addWord("neg");
		addWord("nop");
		addWord("nopl");
		addWord("nopw");
		addWord("or");
		addWord("pop");
		addWord("prefetchw");
		addWord("push");
		addWord("retq");
		addWord("rex");
		addWord("sar");
		addWord("shl");
		addWord("shr");
		addWord("sub");
		addWord("subq");
		addWord("test");
		addWord("testb");
		addWord("ud2");
		addWord("vmovd");
		addWord("vmovdqu");
		addWord("vmovq");
		addWord("vmovsd");
		addWord("vmovss");
		addWord("vpmovzxbw");
		addWord("vzeroupper");
		addWord("xchg");
		addWord("xor");
	}

}
