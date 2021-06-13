package io.jitclipse.jitwatch.core.model;

import java.util.List;

import org.adoptopenjdk.jitwatch.model.assembly.AssemblyBlock;

import io.jitclipse.core.model.IAssemblyInstruction;


public class AssemblyInstruction implements IAssemblyInstruction {

	private ModelContext modelContext;
	private org.adoptopenjdk.jitwatch.model.assembly.AssemblyInstruction instruction;
	private AssemblyBlock assemblyBlock;

	public AssemblyInstruction(ModelContext modelContext, org.adoptopenjdk.jitwatch.model.assembly.AssemblyBlock assemblyBlock,
			org.adoptopenjdk.jitwatch.model.assembly.AssemblyInstruction instruction) {
		this.modelContext = modelContext;
		this.assemblyBlock = assemblyBlock;
		this.instruction = instruction;
	}

	public String getAnnotation() {
		return instruction.getAnnotation();
	}

	public long getAddress() {
		return instruction.getAddress();
	}

	public List<String> getPrefixes() {
		return instruction.getPrefixes();
	}

	public String getMnemonic() {
		return instruction.getMnemonic();
	}

	public List<String> getOperands() {
		return instruction.getOperands();
	}

	public String getComment() {
		return instruction.getComment();
	}

	public List<String> getCommentLines() {
		return instruction.getCommentLines();
	}

	@Override
	public boolean isSafePoint() {
		return instruction.isSafePoint();
	}

	@Override
	public int getLine() {
		return assemblyBlock.getInstructions().indexOf(instruction);
	}

}
