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

import java.util.HashMap;
import java.util.Map;

import org.adoptopenjdk.jitwatch.model.IMetaMember;
import org.adoptopenjdk.jitwatch.model.bytecode.ClassBC;
import org.adoptopenjdk.jitwatch.model.bytecode.MemberBytecode;

import io.jitclipse.core.model.IClassByteCode;
import io.jitclipse.core.model.IMemberByteCode;
import io.jitclipse.core.model.IMethod;

public class ClassByteCode implements IClassByteCode {

	private ModelContext modelContext;
	private ClassBC classBC;

	private Map<IMethod, IMemberByteCode> memberByteCodes = new HashMap<>();

	public ClassByteCode(ModelContext modelContext, ClassBC classBC) {
		this.modelContext = modelContext;
		this.classBC = classBC;
	}

	@Override
	public IMemberByteCode getMemberBytecode(IMethod method) {
		IMemberByteCode memberByteCode;
		if (memberByteCodes.containsKey(method)) {
			memberByteCode = memberByteCodes.get(method);
		} else {
			memberByteCode = doGetMemberByteCode(method);
			memberByteCodes.put(method, memberByteCode);
		}

		return memberByteCode;
	}

	private IMemberByteCode doGetMemberByteCode(IMethod method) {
		try {
			IMetaMember metaMember = modelContext.getJitModelElement(method);
			MemberBytecode memberBytecode = classBC.getMemberBytecode(metaMember);
			return modelContext.getMemberByteCode(memberBytecode);
		} catch (RuntimeException e) {
			return null;
		}
	}

}
