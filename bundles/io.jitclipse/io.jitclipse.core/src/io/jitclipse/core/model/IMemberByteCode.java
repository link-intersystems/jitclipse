package io.jitclipse.core.model;

import java.util.List;

public interface IMemberByteCode extends List<IByteCodeInstruction> {

	public int getSourceLineNr();

	default public IByteCodeInstruction getFirstByteCodeInstruction() {
		if (isEmpty()) {
			return null;
		}
		return get(0);
	}

}
