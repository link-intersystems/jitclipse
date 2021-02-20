package io.jitclipse.core.resources;

import java.util.function.Consumer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import com.link_intersystems.beans.PropertyChangeSource;
import com.link_intersystems.eclipse.core.runtime.IAdaptable2;
import com.link_intersystems.eclipse.core.runtime.IProgress;
import com.link_intersystems.eclipse.core.runtime.jobs.FutureJob;

import io.jitclipse.core.model.IHotspotLog;

public interface IHotspotLogFile extends IAdaptable2, PropertyChangeSource {

	public static final String PROPERTY_HOTSPOT_LOG = "hotspotLog";

	public static boolean isHotspotLogFile(String name) {
		return name.startsWith("hotspot") && name.endsWith(".log");
	}

	public IHotspotLog getHotspotLog();

	IProgress open(Consumer<IHotspotLogFile> hotspotLogFileConsumer, IProgressMonitor progressMonitor);

	public IFile getFile();

	FutureJob<IHotspotLog> createOpenJob(String name);

	default public boolean isOpened() {
		return getHotspotLog() != null;
	}

	public IProgress getProgress();

	default public boolean isLoading() {
		return getProgress() != null;
	}

}
