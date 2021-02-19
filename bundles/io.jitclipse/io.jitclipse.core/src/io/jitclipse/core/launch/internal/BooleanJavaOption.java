package io.jitclipse.core.launch.internal;

public class BooleanJavaOption extends JavaOption {

	private static final String ENABLED_FLAG = "+";
	private static final String DISABLED_FLAG = "-";

	private boolean enabled;

	public BooleanJavaOption(String name) {
		super(name);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		return "-XX:" + getBooleanFlag() + getName();
	}

	private String getBooleanFlag() {
		return isEnabled() ? ENABLED_FLAG : DISABLED_FLAG;
	}

}
