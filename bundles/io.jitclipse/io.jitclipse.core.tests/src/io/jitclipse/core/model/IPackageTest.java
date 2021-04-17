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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

class IPackageTest {

	@Test
	void getRoot() {
		TestPackage rootPackage = new TestPackage();
		TestPackage subPackage = new TestPackage(rootPackage);
		TestPackage subSubPackage = new TestPackage(subPackage);

		assertSame(rootPackage, subSubPackage.getRoot());
		assertSame(rootPackage, subPackage.getRoot());
		assertSame(rootPackage, rootPackage.getRoot());
	}

	@Test
	void containsHotspot() {
		TestPackage rootPackage = new TestPackage();
		TestPackage subPackage1 = new TestPackage(rootPackage);
		TestPackage subPackage2 = new TestPackage(rootPackage);

		IClass aClass = mock(IClass.class);
		IMethodList methodList = mock(IMethodList.class);
		doReturn(methodList).when(aClass).getMethods();
		doReturn(true).when(methodList).containsHotspots();

		subPackage1.addClass(aClass);

		assertTrue(rootPackage.containsHotspots());
		assertTrue(subPackage1.containsHotspots());
		assertFalse(subPackage2.containsHotspots());
	}

}
