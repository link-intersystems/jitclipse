package io.jitclipse.core.model;

import java.util.List;

public interface IAssemblyBlock {
	public String getTitle();

	public List<IAssemblyInstruction> getInstructions();
}
