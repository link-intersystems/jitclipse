package io.jitclipse.jitwatch.core.model;

import java.util.AbstractList;
import java.util.List;

import org.adoptopenjdk.jitwatch.model.bytecode.LineTable;
import org.adoptopenjdk.jitwatch.model.bytecode.MemberBytecode;

import io.jitclipse.core.model.IByteCodeInstruction;
import io.jitclipse.core.model.IMemberByteCode;

public class MemberByteCode extends AbstractList<IByteCodeInstruction> implements IMemberByteCode {

	private MemberBytecode memberBytecode;
	private ModelContext modelContext;

	public MemberByteCode(ModelContext modelContext, MemberBytecode memberBytecode) {
		this.modelContext = modelContext;
		this.memberBytecode = memberBytecode;
	}

	@Override
	public int getSourceLineNr() {
		if (memberBytecode != null) {
			LineTable lineTable = memberBytecode.getLineTable();
			return lineTable.getSourceRange()[0];
		} else {
			return -1;
		}
	}

	private List<IByteCodeInstruction> getByteCodeInstructions() {
		return modelContext.getByteCodeInstructions(memberBytecode);
	}

	@Override
	public IByteCodeInstruction get(int index) {
		return getByteCodeInstructions().get(index);
	}

	@Override
	public int size() {
		return getByteCodeInstructions().size();
	}
}
