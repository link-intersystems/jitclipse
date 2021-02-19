package io.jitclipse.core.parser;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import io.jitclipse.core.model.IHotspotLog;

public interface IJitLogParser {

	public IHotspotLog read(IFile hotspotLogFile, IProgressMonitor monitor) throws Exception;
}
