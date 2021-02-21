package io.jitclipse.core.resources;

import java.util.List;
import java.util.Optional;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

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

	IFolder getFolder();

	default Optional<IHotspotLogFile> getHotspotLogFile(String string) {
		return getHotspotLogfiles().stream().filter(hlf -> hlf.getFile().getName().equals(string)).findFirst();
	}

}
