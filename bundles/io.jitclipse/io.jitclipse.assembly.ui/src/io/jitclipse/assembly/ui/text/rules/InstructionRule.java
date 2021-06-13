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
		addDetectedWord("add");
		addDetectedWord("addq");
		addDetectedWord("and");
		addDetectedWord("callq");
		addDetectedWord("cmove");
		addDetectedWord("cmp");
		addDetectedWord("cmpb");
		addDetectedWord("cmpl");
		addDetectedWord("cmpq");
		addDetectedWord("data16");
		addDetectedWord("dec");
		addDetectedWord("hlt");
		addDetectedWord("inc");
		addDetectedWord("incl");
		addDetectedWord("jae");
		addDetectedWord("jb");
		addDetectedWord("je");
		addDetectedWord("jg");
		addDetectedWord("jge");
		addDetectedWord("jl");
		addDetectedWord("jle");
		addDetectedWord("jmp");
		addDetectedWord("jmpq");
		addDetectedWord("jne");
		addDetectedWord("jno");
		addDetectedWord("lea");
		addDetectedWord("leaveq");
		addDetectedWord("lock");
		addDetectedWord("mov");
		addDetectedWord("movabs");
		addDetectedWord("movb");
		addDetectedWord("movl");
		addDetectedWord("movq");
		addDetectedWord("movsbl");
		addDetectedWord("movslq");
		addDetectedWord("movzbl");
		addDetectedWord("movzwl");
		addDetectedWord("neg");
		addDetectedWord("nop");
		addDetectedWord("nopl");
		addDetectedWord("nopw");
		addDetectedWord("or");
		addDetectedWord("pop");
		addDetectedWord("prefetchw");
		addDetectedWord("push");
		addDetectedWord("retq");
		addDetectedWord("rex");
		addDetectedWord("sar");
		addDetectedWord("shl");
		addDetectedWord("shr");
		addDetectedWord("sub");
		addDetectedWord("subq");
		addDetectedWord("test");
		addDetectedWord("testb");
		addDetectedWord("ud2");
		addDetectedWord("vmovd");
		addDetectedWord("vmovdqu");
		addDetectedWord("vmovq");
		addDetectedWord("vmovsd");
		addDetectedWord("vmovss");
		addDetectedWord("vpmovzxbw");
		addDetectedWord("vzeroupper");
		addDetectedWord("xchg");
		addDetectedWord("xor");
	}

}
