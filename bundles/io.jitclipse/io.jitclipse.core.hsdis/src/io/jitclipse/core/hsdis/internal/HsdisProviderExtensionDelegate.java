package io.jitclipse.core.hsdis.internal;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

import io.jitclipse.core.hsdis.JitCoreHsdisPlugin;
import io.jitclipse.core.launch.Env;
import io.jitclipse.core.launch.HsdisProvider;

public class HsdisProviderExtensionDelegate implements HsdisProvider {

	private HsdisLibraryFolder hsdisLibraryFolder;

	public HsdisProviderExtensionDelegate() {
		this(JitCoreHsdisPlugin.getInstance().getHsdisLibraryFolder());
	}

	public HsdisProviderExtensionDelegate(HsdisLibraryFolder hsdisLibraryFolder) {
		this.hsdisLibraryFolder = Objects.requireNonNull(hsdisLibraryFolder);
	}

	@Override
	public Optional<File> getHsdisLibraryFolder(Env env) {
		return hsdisLibraryFolder.getHsdisLibraryFolder(env);

	}

}
