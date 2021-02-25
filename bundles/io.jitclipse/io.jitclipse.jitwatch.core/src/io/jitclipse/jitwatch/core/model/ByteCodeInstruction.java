package io.jitclipse.jitwatch.core.model;

import org.adoptopenjdk.jitwatch.model.bytecode.BytecodeInstruction;
import org.adoptopenjdk.jitwatch.model.bytecode.LineTable;
import org.adoptopenjdk.jitwatch.model.bytecode.MemberBytecode;
import org.adoptopenjdk.jitwatch.model.bytecode.Opcode;

import io.jitclipse.core.model.IByteCodeInstruction;

public class ByteCodeInstruction implements IByteCodeInstruction {

	private BytecodeInstruction bytecodeInstruction;
	private ModelContext modelContext;
	private MemberBytecode memberBytecode;

	public ByteCodeInstruction(ModelContext modelContext, BytecodeInstruction bytecodeInstruction, MemberBytecode memberBytecode) {
		this.modelContext = modelContext;
		this.bytecodeInstruction = bytecodeInstruction;
		this.memberBytecode = memberBytecode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		LineTable lineTable = memberBytecode.getLineTable();

		int lineNumber = getLineNumber();

		sb.append(lineNumber);
		sb.append(": ");
		Opcode opcode = bytecodeInstruction.getOpcode();
		sb.append(opcode.getMnemonic());

		String comment = bytecodeInstruction.getComment();
		if (comment != null) {
			while (sb.length() < 50) {
				sb.append(' ');
			}

			sb.append("// ");
			sb.append(comment);
		}

		return sb.toString();
	}

	@Override
	public int getLineNumber() {
		// TODO How to
		return bytecodeInstruction.getOffset();
	}

	@Override
	public int getOffset() {
		return bytecodeInstruction.getOffset();
	}
}
