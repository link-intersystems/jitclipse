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

import java.util.List;

import org.adoptopenjdk.jitwatch.model.MetaClass;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IClassByteCode;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IMethodList;
import io.jitclipse.core.model.IPackage;
import io.jitclipse.core.model.MethodList;

public class Class implements IClass {

	private ModelContext modelContext;
	private MetaClass metaClass;
	private IClassByteCode classByteCode;

	public Class(ModelContext modelContext, MetaClass metaClass) {
		this.modelContext = modelContext;
		this.metaClass = metaClass;
	}

	@Override
	public String getName() {
		return metaClass.getFullyQualifiedName();
	}

	@Override
	public IPackage getPackage() {
		return modelContext.getPackage(metaClass.getPackage());
	}

	@Override
	public IMethodList getMethods() {
		List<IMethod> methods = modelContext.getMethods(metaClass.getMetaMembers());
		return new MethodList(methods);
	}

	@Override
	public String getSimpleName() {
		return metaClass.getName();
	}

	@Override
	public IClassByteCode getByteCode() {
		if (classByteCode == null) {
			classByteCode = modelContext.getClassByteCode(metaClass);
		}
		return classByteCode;
	}

}
