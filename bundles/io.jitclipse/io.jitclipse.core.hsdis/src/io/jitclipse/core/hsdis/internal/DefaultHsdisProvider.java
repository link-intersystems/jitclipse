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

import com.link_intersystems.eclipse.core.runtime.runtime.IPluginContext;
import com.link_intersystems.eclipse.core.runtime.runtime.IPluginLog;

import io.jitclipse.core.hsdis.JitCoreHsdisPlugin;
import io.jitclipse.core.launch.Env;
import io.jitclipse.core.launch.HsdisProvider;
import io.jitclipse.core.launch.OperatingSystem;

public class DefaultHsdisProvider implements HsdisProvider {

	private File libraryFolder;

	private Map<OperatingSystem, List<String>> hsdisLibraryNames = new HashMap<>();

	private IPluginContext pluginContext;

	public DefaultHsdisProvider() {
		this(JitCoreHsdisPlugin.getInstance(), JitCoreHsdisPlugin.getInstance().getLibraryFolder());
	}

	public DefaultHsdisProvider(IPluginContext pluginContext, File libraryFolder) {
		this.pluginContext = pluginContext;
		this.libraryFolder = libraryFolder;

		hsdisLibraryNames.put(WINDOWS, asList("hsdis-amd64.dll", "hsdis-i386.dll"));
	}

	@Override
	public Optional<File> getHsdisLibraryFolder(Env env) {
		OperatingSystem os = env.getOperatinSystem();
		String osName = os.name().toLowerCase();

		File osLibraryFolder = new File(libraryFolder, osName);

		if (!osLibraryFolder.exists()) {
			osLibraryFolder.mkdirs();
		}

		List<String> libraryNames = hsdisLibraryNames.getOrDefault(os, emptyList());

		for (String libraryName : libraryNames) {
			File libraryFile = new File(osLibraryFolder, libraryName);
			if (!libraryFile.exists()) {
				try (InputStream libraryInputStream = getClass().getResourceAsStream(libraryName)) {
					copyToFile(libraryInputStream, libraryFile);
				} catch (IOException e) {
					IPluginLog pluginLog = pluginContext.getPluginLog();
					pluginLog.logError("Unable to copy hsdis library " + libraryName + " to " + libraryFile, e);
				}
			}
		}

		if(osLibraryFolder.list().length == 0) {
			return Optional.empty();
		}

		return Optional.of(osLibraryFolder);
	}

}
