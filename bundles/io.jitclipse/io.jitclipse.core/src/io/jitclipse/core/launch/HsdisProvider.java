package io.jitclipse.core.launch;

import java.io.File;
import java.util.Optional;

public interface HsdisProvider {


	public Optional<File> getHsdisLibraryFolder(Env env);
}
