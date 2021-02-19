package io.jitclipse.core.launch;

import org.eclipse.core.resources.IProject;

import io.jitclipse.core.resources.IHotspotLogFile;

public interface IJitLaunch {

	IHotspotLogFile getHotspotLogFile();

	public IProject getProject();

}
