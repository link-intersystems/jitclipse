package io.jitclipse.core.resources.internal;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.link_intersystems.eclipse.core.runtime.IPluginLog;

import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.resources.IHotspotLogFile;
import io.jitclipse.core.resources.IHotspotLogFolder;

public class ProjectHotspotLogFileManager implements IHotspotLogFolder {

	private static final String PROJECT_HOTSPOT_LOCATION = ".hotspot";

	private IPluginLog pluginLog;
	private Clock clock;
	private IProject project;
	private IFolder hotspotLogFileFolder;

	public ProjectHotspotLogFileManager(Clock clock, IProject project) {
		this(JitCorePlugin.getInstance().getPluginLog(), clock, project);
	}

	public ProjectHotspotLogFileManager(IPluginLog pluginLog, Clock clock, IProject project) {
		this.pluginLog = pluginLog;
		this.clock = clock;
		this.project = project;
		hotspotLogFileFolder = project.getFolder(PROJECT_HOTSPOT_LOCATION);
		try {
			if (!hotspotLogFileFolder.exists()) {
				hotspotLogFileFolder.create(true, true, new NullProgressMonitor());
			}
		} catch (CoreException e) {
			pluginLog.logError(e);
		}
	}

	@Override
	public IFile newHotspotLogFile() {
		String projectName = project.getName();
		if (projectName == null) {
			return null;
		}

		String hotspotLogFilename = getHotspotLogFilename();

		if (!hotspotLogFileFolder.exists()) {
			try {
				hotspotLogFileFolder.create(true, false, null);
			} catch (CoreException e) {
				pluginLog.logError(e);
			}
		}

		IFile hotspotLogFile = hotspotLogFileFolder.getFile(hotspotLogFilename);
		try {
			hotspotLogFileFolder.refreshLocal(1, new NullProgressMonitor());
		} catch (CoreException e) {
			pluginLog.logError(e);
		}
		return hotspotLogFile;
	}

	private String getHotspotLogFilename() {
		Date now = new Date(clock.millis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = dateFormat.format(now);
		String hotspotLogFilename = "hotspot-" + timestamp + ".log";
		return hotspotLogFilename;
	}

	@Override
	public List<IHotspotLogFile> getHotspotLogfiles() {
		List<IHotspotLogFile> hotspotLogFiles = new ArrayList<>();

		List<IFile> files = getFiles();
		for (IFile file : files) {
			IHotspotLogFile hotspotLogFile = file.getAdapter(IHotspotLogFile.class);
			if (hotspotLogFile != null) {
				hotspotLogFiles.add(hotspotLogFile);
			}
		}

		return hotspotLogFiles;
	}

	@Override
	public List<IFile> getFiles() {
		List<IFile> files = new ArrayList<>();

		try {
			IResource[] members = hotspotLogFileFolder.members();
			for (IResource member : members) {
				IFile file = member.getAdapter(IFile.class);
				if (file != null) {
					files.add(file);
				}
			}
		} catch (CoreException e) {
			pluginLog.logError(e);
		}

		return files;
	}

}
