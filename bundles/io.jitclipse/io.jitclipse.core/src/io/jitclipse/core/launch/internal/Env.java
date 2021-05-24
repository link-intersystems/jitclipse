package io.jitclipse.core.launch.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class Env {

	public static final Env WINDOWS = new Env('\"', ";");
	public static final Env LINUX = new Env('\"', ":");
	public static final Env MAC = new Env('\"', ":");

	private static String OS = System.getProperty("os.name").toLowerCase();

	public static Optional<Env> getCurrent() {
		Env env = null;

		if (isWindows()) {
			env = WINDOWS;
		} else if (isMac()) {
			env = MAC;
		} else if (isUnix()) {
			env = LINUX;
		}
		return Optional.ofNullable(env);

	}

	public static boolean isWindows() {
		return OS.contains("win");
	}

	public static boolean isMac() {
		return OS.contains("mac");
	}

	public static boolean isUnix() {
		return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
	}

	public static boolean isSolaris() {
		return OS.contains("sunos");
	}

	public static String getOS() {
		if (isWindows()) {
			return "win";
		} else if (isMac()) {
			return "osx";
		} else if (isUnix()) {
			return "uni";
		} else if (isSolaris()) {
			return "sol";
		} else {
			return "err";
		}
	}

	private char enclosingChar = '\"';
	private String pathSeparator = ";";

	Env(char enclosingChar, String pathSeparator) {
		super();
		this.enclosingChar = enclosingChar;
		this.pathSeparator = pathSeparator;
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
