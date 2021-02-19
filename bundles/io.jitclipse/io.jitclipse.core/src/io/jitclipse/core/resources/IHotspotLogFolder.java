package io.jitclipse.core.resources;

import java.util.List;

import org.eclipse.core.resources.IFile;

import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IPackage;

public interface IHotspotLogFolder {

	public default boolean exists() {
		return getHotspotLogfiles().size() > 0;
	}

	List<IHotspotLogFile> getHotspotLogfiles();

	IFile newHotspotLogFile();

	default IHotspotLogFile getHotspotLogFile(IPackage aPackage) {
		List<IHotspotLogFile> hotspotLogfiles = getHotspotLogfiles();
		for (IHotspotLogFile hotspotLogFile : hotspotLogfiles) {
			IHotspotLog hotspotLog = hotspotLogFile.getHotspotLog();
			if (hotspotLog != null) {
				if (hotspotLog.contains(aPackage)) {
					return hotspotLogFile;
				}
			}
		}
		return null;
	}

	List<IFile> getFiles();

}
