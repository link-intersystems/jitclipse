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
package io.jitclipse.jitwatch.core.model;

import org.adoptopenjdk.jitwatch.model.bytecode.LineTable;
import org.adoptopenjdk.jitwatch.model.bytecode.MemberBytecode;

import io.jitclipse.core.model.IMemberByteCode;

public class MemberByteCode implements IMemberByteCode {

	private MemberBytecode memberBytecode;

	public MemberByteCode(ModelContext modelContext, MemberBytecode memberBytecode) {
		this.memberBytecode = memberBytecode;
	}

	@Override
	public int getSourceLineNr() {
		if (memberBytecode != null) {
			LineTable lineTable = memberBytecode.getLineTable();
			return lineTable.getSourceRange()[0];
		} else {
			return -1;
		}
	}

	@Override
	public int getByteCodeInstruction() {
		return 0;
	}
}
