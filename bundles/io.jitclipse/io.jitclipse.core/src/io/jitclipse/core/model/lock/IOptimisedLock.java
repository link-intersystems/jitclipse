package io.jitclipse.core.model.lock;

import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.IMethod;

public interface IOptimisedLock {

	IMethod getMethod();

	ICompilation getCompilation();

	String getHow();

	String getKind();

}
