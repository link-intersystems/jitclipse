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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.jitclipse.core.model.ICompilation;

class CompilationListTest {

	private ICompilation compilation1;
	private ICompilation compilation2;
	private List<ICompilation> compilations;

	@BeforeEach
	public void setup() {
		compilation1 = Mockito.mock(ICompilation.class);
		compilation2 = Mockito.mock(ICompilation.class);
		compilations = Arrays.asList(compilation1, compilation2);

	}

	@Test
	void ensureAllElementsContained() {
		CompilationList compilationList = new CompilationList(compilations);

		assertEquals(compilations, compilationList);
	}

	@Test
	void idDefaultOrder() {
		Mockito.when(compilation1.getId()).thenReturn(1L);
		Mockito.when(compilation2.getId()).thenReturn(0L);
		CompilationList compilationList = new CompilationList(compilations);

		assertEquals(compilation2, compilationList.get(0));
		assertEquals(compilation1, compilationList.get(1));
	}

}
