package io.jitclipse.core.model;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
		when(aClass.containsHotspots()).thenReturn(true);

		subPackage1.addClass(aClass);

		assertTrue(rootPackage.containsHotspots());
		assertTrue(subPackage1.containsHotspots());
		assertFalse(subPackage2.containsHotspots());
	}

}
