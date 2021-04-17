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
package io.jitclipse.core.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MethodList extends AbstractList<IMethod> implements IMethodList {

	private List<IMethod> methods = new ArrayList<>();

	public MethodList(List<IMethod> methods) {
		this.methods.addAll(methods);
	}

	@Override
	public Optional<IMethod> findByName(String name) {
		return find(c -> c.getName().equals(name));
	}

	@Override
	public Optional<IMethod> find(Predicate<IMethod> predicate) {
		return stream().filter(predicate).findFirst();
	}

	@Override
	public IMethod get(int index) {
		return methods.get(index);
	}

	@Override
	public int size() {
		return methods.size();
	}

}
