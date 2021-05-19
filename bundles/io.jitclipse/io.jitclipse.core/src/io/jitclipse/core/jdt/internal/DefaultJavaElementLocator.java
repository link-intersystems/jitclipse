package io.jitclipse.core.jdt.internal;

import java.util.Objects;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.DefaultWorkingCopyOwner;

import io.jitclipse.core.jdt.JavaElementLocator;

@SuppressWarnings("restriction")
public class DefaultJavaElementLocator implements JavaElementLocator{

	private IJavaProject javaProject;

	public DefaultJavaElementLocator(IJavaProject javaProject) {
		this.javaProject = Objects.requireNonNull(javaProject);
	}

	@Override
	public IType findType(String fullQualifiedTypeName) throws JavaModelException {
		return javaProject.findType(fullQualifiedTypeName, DefaultWorkingCopyOwner.PRIMARY, null);
	}

}
