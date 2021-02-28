package io.jitclipse.core.launch.internal;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public interface IOutputGrabber {

	void setWait(Duration wait);

	void setGrabErrorOutput(boolean grabErrorOutput);

	String grabOutput(File exec, String... args) throws IOException;

}