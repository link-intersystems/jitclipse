package io.jitclipse.jitwatch.core.model;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.adoptopenjdk.jitwatch.model.JITDataModel;
import org.adoptopenjdk.jitwatch.model.MetaClass;
import org.adoptopenjdk.jitwatch.model.MetaMethod;
import org.adoptopenjdk.jitwatch.model.MetaPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IPackage;

class ModelContextTest {

	private JITDataModel jitDataModel;
	private ModelContext modelContext;

	@BeforeEach
	public void setup() {
		jitDataModel = new JITDataModel();
		modelContext = new ModelContext(jitDataModel);
	}

	@Test
	void getSameClass() {
		MetaClass metaClass = mock(MetaClass.class);

		IClass class1 = modelContext.getClass(metaClass);
		IClass class2 = modelContext.getClass(metaClass);

		assertSame(class1, class2);
	}

	@Test
	void getSameMethod() {
		MetaMethod metaMethod = mock(MetaMethod.class);

		IMethod method1 = modelContext.getMethod(metaMethod);
		IMethod method2 = modelContext.getMethod(metaMethod);

		assertSame(method1, method2);
	}

	@Test
	void getSamePackage() {
		MetaPackage metaPackage = mock(MetaPackage.class);

		IPackage package1 = modelContext.getPackage(metaPackage);
		IPackage package2 = modelContext.getPackage(metaPackage);

		assertSame(package1, package2);
	}

	@Test
	void getSameCompilations() {
		org.adoptopenjdk.jitwatch.model.Compilation compilation1 = mock(
				org.adoptopenjdk.jitwatch.model.Compilation.class);

		org.adoptopenjdk.jitwatch.model.Compilation compilation2 = mock(
				org.adoptopenjdk.jitwatch.model.Compilation.class);

		List<ICompilation> compilations1 = modelContext.getCompilations(Arrays.asList(compilation1, compilation2));
		List<ICompilation> compilations2 = modelContext.getCompilations(Arrays.asList(compilation1, compilation2));

		assertEquals(2, compilations1.size());
		assertEquals(compilations1.size(), compilations2.size());

		for (int i = 0; i < compilations1.size(); i++) {
			ICompilation iCompilation1 = compilations1.get(i);
			ICompilation iCompilation2 = compilations2.get(i);

			assertSame(iCompilation1, iCompilation2);
		}
	}

}
