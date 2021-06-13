package io.jitclipse.core.model;

import java.util.List;

public interface IAssemblyInstruction {
	public String getAnnotation();

	public long getAddress();

	public List<String> getPrefixes();

	public String getMnemonic();

	public List<String> getOperands();

	public String getComment();

	public List<String> getCommentLines();

	public boolean isSafePoint();

	public int getLine();
}
