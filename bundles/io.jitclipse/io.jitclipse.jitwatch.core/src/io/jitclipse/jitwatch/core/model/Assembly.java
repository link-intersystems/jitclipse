package io.jitclipse.jitwatch.core.model;

import org.adoptopenjdk.jitwatch.model.assembly.AssemblyMethod;

import io.jitclipse.core.model.IAssembly;

public class Assembly implements IAssembly {

	private ModelContext modelContext;
	private AssemblyMethod assemblyMethod;

	public Assembly(ModelContext modelContext, AssemblyMethod assemblyMethod) {
		this.modelContext = modelContext;
		this.assemblyMethod = assemblyMethod;
	}

}
