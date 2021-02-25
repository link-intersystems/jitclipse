package io.jitclipse.core.launch;

import java.util.Iterator;
import java.util.LinkedHashMap;

import io.jitclipse.core.launch.internal.BooleanJavaOption;
import io.jitclipse.core.launch.internal.JavaOption;
import io.jitclipse.core.launch.internal.StringJavaOption;

public abstract class AbstractJitArgs implements IJitArgs {

	private LinkedHashMap<String, JavaOption> javaOptions = new LinkedHashMap<>();

	@Override
	public boolean isEmpty() {
		return javaOptions.isEmpty();
	}

	protected void setStringOption(String name, String vale) {
		StringJavaOption stringJavaOption = new StringJavaOption(name);
		stringJavaOption.setValue(vale);
		javaOptions.put(name, stringJavaOption);
	}

	protected void setBooleanOption(String name, boolean enabled) {
		BooleanJavaOption booleanJavaOption = new BooleanJavaOption(name);
		booleanJavaOption.setEnabled(enabled);
		javaOptions.put(name, booleanJavaOption);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		Iterator<JavaOption> optionsIterator = javaOptions.values().iterator();

		while (optionsIterator.hasNext()) {
			JavaOption javaOption = optionsIterator.next();
			sb.append(javaOption);
			if (optionsIterator.hasNext()) {
				sb.append(' ');
			}
		}

		return sb.toString();
	}
}