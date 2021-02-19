package io.jitclipse.jitwatch.core.model;

import org.adoptopenjdk.jitwatch.model.bytecode.LineTable;
import org.adoptopenjdk.jitwatch.model.bytecode.MemberBytecode;

import io.jitclipse.core.model.IMemberByteCode;

public class MemberByteCode implements IMemberByteCode {

	private MemberBytecode memberBytecode;

	public MemberByteCode(ModelContext modelContext, MemberBytecode memberBytecode) {
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

	@Override
	public int getByteCodeInstruction() {
		return 0;
	}
}
