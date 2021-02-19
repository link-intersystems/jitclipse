package io.jitclipse.core.model.allocation;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IOptimization;

public interface IEliminatedAllocation {

	IMethod getMethod();

	ICompilation getCompilation();

	IOptimization getOptimization();

	IClass getEliminatedType();
}
