package io.jitclipse.core.launch.internal;

public abstract class JavaOption {

	private String name;

	public JavaOption(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
