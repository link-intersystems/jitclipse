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

import java.util.AbstractList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.ICompilationList;

public class CompilationList extends AbstractList<ICompilation> implements ICompilationList {

	public static class IdComparator implements Comparator<ICompilation> {

		@Override
		public int compare(ICompilation o1, ICompilation o2) {
			long id1 = o1.getId();
			long id2 = o2.getId();

			return Long.compare(id1, id2);
		}

	}

	private List<ICompilation> compilations;

	public CompilationList(List<ICompilation> compilations) {
		this(compilations, new IdComparator());
	}

	public CompilationList(List<ICompilation> compilations, Comparator<ICompilation> comparator) {
		this.compilations = compilations;
		Collections.sort(compilations, comparator);
	}

	@Override
	public ICompilation get(int index) {
		return compilations.get(index);
	}

	@Override
	public int size() {
		return compilations.size();
	}

}
