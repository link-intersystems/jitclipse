package io.jitclipse.assembly.ui.format;

public class DefaultFormatConfig implements IFormatConfig {

	private boolean useLocalLabels = false;

	private String newLine = "\n";

	@Override
	public String getNewLine() {
		return newLine;
	}

	@Override
	public boolean isUseLocalLabels() {
		return useLocalLabels;
	}



}
