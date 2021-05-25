package io.jitclipse.core.hsdis.internal;

import static io.jitclipse.core.launch.OperatingSystem.WINDOWS;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.apache.commons.io.FileUtils.copyToFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.link_intersystems.eclipse.core.runtime.runtime.IPluginLog;

import io.jitclipse.core.launch.Env;
import io.jitclipse.core.launch.HsdisProvider;
import io.jitclipse.core.launch.OperatingSystem;

public class HsdisLibraryFolder implements HsdisProvider {

	private Map<OperatingSystem, List<String>> hsdisLibraryNames = new HashMap<>();

	private File baseFolder;

	private IPluginLog pluginLog;

	public HsdisLibraryFolder(File baseFolder, IPluginLog pluginLog) {
		this.baseFolder = baseFolder;
		this.pluginLog = pluginLog;

		hsdisLibraryNames.put(WINDOWS, asList("hsdis-amd64.dll", "hsdis-i386.dll"));
	}

	public void init() {
		for (Env env : Env.VALUES) {
			initEnv(env);
		}
	}

	private void initEnv(Env env) {
		OperatingSystem os = env.getOperatinSystem();
		File osLibraryFolder = getOsLibraryFolder(os);

		List<String> libraryNames = hsdisLibraryNames.getOrDefault(os, emptyList());

		for (String libraryName : libraryNames) {
			File libraryFile = new File(osLibraryFolder, libraryName);
			if (!libraryFile.exists()) {
				try (InputStream libraryInputStream = getClass().getResourceAsStream(libraryName)) {
					copyToFile(libraryInputStream, libraryFile);
				} catch (IOException e) {
					pluginLog.logError("Unable to copy hsdis library " + libraryName + " to " + libraryFile, e);
				}
			}
		}
	}

	private File getOsLibraryFolder(Env env) {
		OperatingSystem os = env.getOperatinSystem();

		File osLibraryFolder = getOsLibraryFolder(os);
		return osLibraryFolder;
	}

	private File getOsLibraryFolder(OperatingSystem os) {
		String osName = os.name().toLowerCase();
		File osLibraryFolder = new File(baseFolder, osName);

		if (!osLibraryFolder.exists()) {
			osLibraryFolder.mkdirs();
		}

		return osLibraryFolder;
	}

	private boolean isInitialized(Env env) {
		OperatingSystem os = env.getOperatinSystem();

		File osLibraryFolder = getOsLibraryFolder(os);

		List<String> libraryNames = hsdisLibraryNames.getOrDefault(os, emptyList());
		List<String> libraryFiles = asList(osLibraryFolder.list((f, n) -> libraryNames.contains(n)));

		return libraryFiles.size() == libraryNames.size();
	}

	@Override
	public Optional<File> getHsdisLibraryFolder(Env env) {
		if (isInitialized(env)) {
			return Optional.of(getOsLibraryFolder(env));
		}
		return Optional.empty();
	}
}
