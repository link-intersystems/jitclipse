package io.jitclipse.core.launch;

import java.util.Optional;

public enum OperatingSystem {

	WINDOWS, LINUX, SOLARIS, MAC, UNKNOWN;

	public static Optional<OperatingSystem> getCurrent() {

		String osName = System.getProperty("os.name").toLowerCase();

		return getByName(osName);
	}

	public static Optional<OperatingSystem> getByName(String osName) {
		OperatingSystem operatingSystem = null;

		if (osName.contains("win")) {
			operatingSystem = WINDOWS;
		} else if ((osName.contains("nix") || osName.contains("nux") || osName.contains("aix"))) {
			operatingSystem = LINUX;
		} else if (osName.contains("mac")) {
			operatingSystem = MAC;
		} else if (osName.contains("sunos")) {
			operatingSystem = SOLARIS;
		}

		return Optional.ofNullable(operatingSystem);
	}

}
