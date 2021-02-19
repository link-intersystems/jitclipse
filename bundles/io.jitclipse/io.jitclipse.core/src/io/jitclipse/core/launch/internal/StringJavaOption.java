
package io.jitclipse.core.launch.internal;

public class StringJavaOption extends JavaOption {

	private String value;

	public StringJavaOption(String name) {
		super(name);
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "-XX:" + getName() + "=\"" + value + "\"";
	}

}
