package io.jitclipse.jitwatch.core.model;

import java.util.List;

import org.adoptopenjdk.jitwatch.model.assembly.AssemblyMethod;

import io.jitclipse.core.model.Architecture;
import io.jitclipse.core.model.IAssembly;
import io.jitclipse.core.model.IAssemblyBlock;

public class Assembly implements IAssembly {

	private ModelContext modelContext;
	private AssemblyMethod assemblyMethod;

	public Assembly(ModelContext modelContext, AssemblyMethod assemblyMethod) {
		this.modelContext = modelContext;
		this.assemblyMethod = assemblyMethod;
	}

	@Override
	public Architecture getArchitecture() {
		return Architecture.valueOf(this.assemblyMethod.getArchitecture().name());
	}

	@Override
	public String getAssemblyMethodSignature() {
		return this.assemblyMethod.getAssemblyMethodSignature();
	}

	@Override
	public String getHeader() {
		return this.assemblyMethod.getHeader();
	}

	@Override
	public List<IAssemblyBlock> getBlocks() {
		return modelContext.getAssemblyBlocks(this.assemblyMethod.getBlocks());
	}

	@Override
	public String getNativeAddress() {
		return this.assemblyMethod.getNativeAddress();
	}

	@Override
	public String getEntryAddress() {
		return this.assemblyMethod.getEntryAddress();
	}

}
