package io.jitclipse.jitwatch.core.launch;

import java.io.File;

import io.jitclipse.core.launch.AbstractJitArgs;

public class OpenJDKJitArgs extends AbstractJitArgs {

	@Override
	public void setHotspotLogEnabled(boolean enableHostspotLog) {
		setBooleanOption("UnlockDiagnosticVMOptions", enableHostspotLog);
		setBooleanOption("TraceClassLoading", enableHostspotLog);
		setBooleanOption("LogCompilation", enableHostspotLog);
	}

	@Override
	public void setHotspotLogFile(File hotspotLogFile) {
		setStringOption("LogFile", hotspotLogFile.toString());
	}

	@Override
	public void setDisassembledCodeEnabled(boolean disassembledCode) {
		setBooleanOption("PrintAssembly", disassembledCode);
	}

	@Override
	public void setClassModelEnabled(boolean classModel) {
		setBooleanOption("TraceClassLoading ", classModel);
	}

}
