package io.jitclipse.jitwatch.core.model;

import java.util.List;

import io.jitclipse.core.model.IAssemblyBlock;
import io.jitclipse.core.model.IAssemblyInstruction;

public class AssemblyBlock implements IAssemblyBlock {

	private ModelContext modelContext;
	private org.adoptopenjdk.jitwatch.model.assembly.AssemblyBlock assemblyBlock;

	public AssemblyBlock(ModelContext modelContext,
			org.adoptopenjdk.jitwatch.model.assembly.AssemblyBlock assemblyBlock) {
		this.modelContext = modelContext;
		this.assemblyBlock = assemblyBlock;
	}

	@Override
	public String getTitle() {
		return this.assemblyBlock.getTitle();
	}

	@Override
	public List<IAssemblyInstruction> getInstructions() {
		return modelContext.getAssemblyInstructions(assemblyBlock);
	}

}
