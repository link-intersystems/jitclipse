package io.jitclipse.core.launch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EnvPath implements Iterable<String> {

	private List<String> pathElements = new ArrayList<>();

	public EnvPath() {
	}

	public EnvPath(List<String> pathElements) {
		this.pathElements = pathElements;
	}

	public boolean contains(String path) {
		return pathElements.contains(path);
	}

	@Override
	public Iterator<String> iterator() {
		return Collections.unmodifiableList(pathElements).iterator();
	}

	public void add(String pathElement) {
		pathElements.add(pathElement);
	}

	public int size() {
		return pathElements.size();
	}

}
