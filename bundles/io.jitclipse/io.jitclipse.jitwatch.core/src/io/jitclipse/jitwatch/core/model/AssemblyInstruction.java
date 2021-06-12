package io.jitclipse.jitwatch.core.model;

import java.util.List;

import io.jitclipse.core.model.IAssemblyInstruction;

public class AssemblyInstruction implements IAssemblyInstruction {

	private ModelContext modelContext;
	private org.adoptopenjdk.jitwatch.model.assembly.AssemblyInstruction assemblyInstruction;

	public AssemblyInstruction(ModelContext modelContext,
			org.adoptopenjdk.jitwatch.model.assembly.AssemblyInstruction assemblyInstruction) {
		this.modelContext = modelContext;
		this.assemblyInstruction = assemblyInstruction;
	}

	public String getAnnotation() {
		return assemblyInstruction.getAnnotation();
	}

	public long getAddress() {
		return assemblyInstruction.getAddress();
	}

	public List<String> getPrefixes() {
		return assemblyInstruction.getPrefixes();
	}

	public String getMnemonic() {
		return assemblyInstruction.getMnemonic();
	}

	public List<String> getOperands() {
		return assemblyInstruction.getOperands();
	}

	public String getComment() {
		return assemblyInstruction.getComment();
	}

	public List<String> getCommentLines() {
		return assemblyInstruction.getCommentLines();
	}

	@Override
	public boolean isSafePoint() {
		return assemblyInstruction.isSafePoint();
	}

}
