package io.jitclipse.jitwatch.core.launch;

import java.io.File;

import io.jitclipse.core.launch.AbstractJitArgs;

public class OpenJDKJitArgs extends AbstractJitArgs {

	@Override
	public void setHotspotLogFile(File hotspotLogFile) {
		setHotspotLogEnabled();
		setStringOption("LogFile", hotspotLogFile.toString());
	}

	private void setHotspotLogEnabled() {
		setBooleanOption("UnlockDiagnosticVMOptions", true);
		setBooleanOption("TraceClassLoading", true);
		setBooleanOption("LogCompilation", true);
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
