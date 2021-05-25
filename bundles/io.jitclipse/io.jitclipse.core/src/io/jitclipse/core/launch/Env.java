package io.jitclipse.core.launch;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class Env {

	public static final Env WINDOWS = new Env(OperatingSystem.WINDOWS, '\"', ";");
	public static final Env LINUX = new Env(OperatingSystem.LINUX, '\"', ":");
	public static final Env MAC = new Env(OperatingSystem.MAC, '\"', ":");

	public static final List<Env> VALUES = unmodifiableList(asList(WINDOWS, LINUX, MAC));

	private static final Map<OperatingSystem, Env> envMapping;

	static {
		Map<OperatingSystem, Env> tempEnvMapping = new HashMap<>();

		tempEnvMapping.put(OperatingSystem.WINDOWS, WINDOWS);
		tempEnvMapping.put(OperatingSystem.LINUX, LINUX);
		tempEnvMapping.put(OperatingSystem.MAC, MAC);

		envMapping = Collections.unmodifiableMap(tempEnvMapping);
	}

	public static Optional<Env> getCurrent() {
		Optional<OperatingSystem> currentOs = OperatingSystem.getCurrent();
		Optional<Env> env = currentOs.map(envMapping::get);
		return env;
	}

	private char enclosingChar = '\"';
	private String pathSeparator = ";";
	private OperatingSystem os;

	private Env(OperatingSystem os, char enclosingChar, String pathSeparator) {
		this.os = os;
		this.enclosingChar = enclosingChar;
		this.pathSeparator = pathSeparator;
	}

	public OperatingSystem getOperatinSystem() {
		return os;
	}

	public EnvPath parsePath(String pathSpec) {
		List<String> envPathElements = new ArrayList<>();

		String[] pathElements = pathSpec.split(Pattern.quote(pathSeparator));
		for (int i = 0; i < pathElements.length; i++) {
			String pathElement = pathElements[i];
			if (!pathElement.isBlank()) {
				if (isEnclosed(pathElement)) {
					pathElement = pathElement.substring(1).substring(0, pathElement.length() - 2);
				}

				envPathElements.add(pathElement);
			}
		}

		return new EnvPath(envPathElements);
	}

	private boolean isEnclosed(String pathElement) {
		if (pathElement.length() < 2) {
			return false;
		}

		char firstChar = pathElement.charAt(0);
		char lastChar = pathElement.charAt(pathElement.length() - 1);

		return firstChar == enclosingChar && lastChar == enclosingChar;
	}

	public String formatPath(EnvPath envPath) {
		StringBuilder sb = new StringBuilder();

		Iterator<String> pathElementsIterator = envPath.iterator();
		while (pathElementsIterator.hasNext()) {
			String nextEnvPathElement = pathElementsIterator.next();

			if (nextEnvPathElement.contains(" ")) {
				nextEnvPathElement = enclosingChar + nextEnvPathElement + enclosingChar;
			}

			sb.append(nextEnvPathElement);

			if (pathElementsIterator.hasNext()) {
				sb.append(pathSeparator);
			}
		}

		return sb.toString();
	}

}
