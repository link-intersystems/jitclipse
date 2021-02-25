package io.jitclipse.core.launch;

import java.io.File;

public interface IJitArgs {

	public void setHotspotLogEnabled(boolean enableHostspotLog);

	public void setHotspotLogFile(File hotspotLogFile);

	public void setDisassembledCodeEnabled(boolean disassembledCode);

	public void setClassModelEnabled(boolean classModel);

	public String toString();

	public boolean isEmpty();

}
