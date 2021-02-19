package io.jitclipse.core.resources;

import java.util.function.Consumer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import com.link_intersystems.eclipse.core.runtime.IAdaptable2;
import com.link_intersystems.eclipse.core.runtime.jobs.FutureJob;

import io.jitclipse.core.model.IHotspotLog;

public interface IHotspotLogFile extends IAdaptable2 {

	public static interface IHotspotLogFileObservable {

		public void onHotspotLog(Consumer<IHotspotLog> hotspotLogConsumer);
	}

	public static boolean isHotspotLogFile(String name) {
		return name.startsWith("hotspot") && name.endsWith(".log");
	}

	public IHotspotLog getHotspotLog();

	void open(Consumer<IHotspotLogFile> hotspotLogFileConsumer, IProgressMonitor progressMonitor);

	public IFile getFile();

	FutureJob<IHotspotLog> createOpenJob(String name);

}
