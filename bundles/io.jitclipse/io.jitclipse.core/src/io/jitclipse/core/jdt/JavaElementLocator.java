package io.jitclipse.core.jdt;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public interface JavaElementLocator {

	IType findType(String fullQualifiedTypeName) throws JavaModelException;


}
