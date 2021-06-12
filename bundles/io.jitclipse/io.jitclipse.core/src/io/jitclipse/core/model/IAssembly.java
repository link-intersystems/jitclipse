package io.jitclipse.core.model;

import java.util.List;


public interface IAssembly {

	public Architecture getArchitecture();

	public String getAssemblyMethodSignature();

	public String getHeader();

	public List<IAssemblyBlock> getBlocks();

	public String getNativeAddress();


	public String getEntryAddress();

}
