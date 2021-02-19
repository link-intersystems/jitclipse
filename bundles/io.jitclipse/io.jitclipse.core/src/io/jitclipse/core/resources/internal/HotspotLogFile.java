package io.jitclipse.core.resources.internal;

import java.util.function.Consumer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;

import com.link_intersystems.eclipse.core.runtime.PluginLog;
import com.link_intersystems.eclipse.core.runtime.jobs.FutureJob;

import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.parser.IJitLogParser;
import io.jitclipse.core.resources.IHotspotLogFile;
import io.jitclipse.core.resources.IJitProject;

public class HotspotLogFile implements IHotspotLogFile {

	private static final QualifiedName HOTSPOT_LOG_FILE = new QualifiedName(JitCorePlugin.ID, "hotspotLogFile");

	private IFile file;
	private IHotspotLog hotspotLog;

	public HotspotLogFile(IFile file) {
		this.file = file;
	}

	public static boolean isLogFilename(IFile file) {
		return file.getName().startsWith("hotspot") && file.getFileExtension().equals("log");
	}

	@Override
	public IHotspotLog getHotspotLog() {
		return hotspotLog;
	}

	void setHotspotLog(IHotspotLog hotspotLog) {
		this.hotspotLog = hotspotLog;
	}

	@Override
	public void open(Consumer<IHotspotLogFile> hotspotLogFileConsumer, IProgressMonitor monitor) {
		if (hotspotLog == null) {
			FutureJob<IHotspotLog> futureJob = createOpenJob("Open Hotspot Log");
			futureJob.addProgressListener(monitor);
			futureJob.whenDone(hl -> {
				setHotspotLog(hl);
				hotspotLogFileConsumer.accept(HotspotLogFile.this);
			});

			futureJob.schedule();
		} else {
			hotspotLogFileConsumer.accept(this);
		}
	}

	@Override
	public FutureJob<IHotspotLog> createOpenJob(String name) {
		FutureJob<IHotspotLog> futureJob = new FutureJob<>(name, this::callParser);
		futureJob.whenDone(this::setHotspotLog);
		return futureJob;
	}

	private IHotspotLog callParser(IProgressMonitor monitor) throws Exception {
		IProject project = file.getProject();
		IJitProject jitProject = project.getAdapter(IJitProject.class);
		IJitLogParser parser = jitProject.getParser();
		return parser.read(file, monitor);
	}

	@Override
	public IFile getFile() {
		return file;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (IFile.class.equals(adapter)) {
			return adapter.cast(getFile());
		}
		return IHotspotLogFile.super.getAdapter(adapter);
	}

	public static IHotspotLogFile getForFile(IFile file) {
		HotspotLogFile hotspotLogFile = null;
		try {
			if(file.exists()) {
				hotspotLogFile = (HotspotLogFile) file.getSessionProperty(HOTSPOT_LOG_FILE);
			}

			if (hotspotLogFile == null) {
				hotspotLogFile = new HotspotLogFile(file);
				hotspotLogFile.setForFile(file);
			}
			return hotspotLogFile;
		} catch (CoreException e) {
			JitCorePlugin jitCorePlugin = JitCorePlugin.getInstance();
			PluginLog pluginLog = jitCorePlugin.getPluginLog();
			pluginLog.logError(e);
		}
		return hotspotLogFile;
	}

	private void setForFile(IFile file) {
		try {
			file.setSessionProperty(HOTSPOT_LOG_FILE, this);
		} catch (CoreException e) {
			JitCorePlugin jitCorePlugin = JitCorePlugin.getInstance();
			PluginLog pluginLog = jitCorePlugin.getPluginLog();
			pluginLog.logError(e);
		}
	}

}
