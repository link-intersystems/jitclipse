/*******************************************************************************
 * Copyright (c) 2021 Link Intersystems GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Link Intersystems GmbH - René Link - API and implementation
 *******************************************************************************/
package io.jitclipse.core.resources.internal;

import java.util.function.Consumer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;

import com.link_intersystems.beans.AbstractPropertyChangeSource;
import com.link_intersystems.eclipse.core.runtime.runtime.PluginLog;
import com.link_intersystems.eclipse.core.runtime.runtime.jobs.FutureJob;
import com.link_intersystems.eclipse.core.runtime.runtime.progress.ETAProgressMonitor;
import com.link_intersystems.eclipse.core.runtime.runtime.progress.IProgress;
import com.link_intersystems.eclipse.core.runtime.runtime.progress.ProgressIndicator;

import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.parser.IJitLogParser;
import io.jitclipse.core.resources.IHotspotLogFile;
import io.jitclipse.core.resources.IJitProject;

public class HotspotLogFile extends AbstractPropertyChangeSource implements IHotspotLogFile {

	private static final QualifiedName HOTSPOT_LOG_FILE = new QualifiedName(JitCorePlugin.ID, "hotspotLogFile");

	private IFile file;
	private IHotspotLog hotspotLog;

	private FutureJob<IHotspotLog> futureJob;

	private ProgressIndicator progressIndicator;

	public HotspotLogFile(IFile file) {
		this.file = file;
	}

	@Override
	public IHotspotLog getHotspotLog() {
		return hotspotLog;
	}

	void setHotspotLog(IHotspotLog hotspotLog) {
		firePropertyChange(PROPERTY_HOTSPOT_LOG, this.hotspotLog, this.hotspotLog = hotspotLog);
	}

	@Override
	public IProgress open(Consumer<IHotspotLogFile> hotspotLogFileConsumer, IProgressMonitor monitor) {
		if (futureJob == null) {
			futureJob = createOpenJob("Open Hotspot Log");
			progressIndicator = new ProgressIndicator();
			ETAProgressMonitor etaProgressMonitor = new ETAProgressMonitor(progressIndicator);
			etaProgressMonitor.setEtaPattern("{1} - {0}");
			futureJob.addProgressListener(etaProgressMonitor);
			if (monitor != null) {
				futureJob.addProgressListener(monitor);
			}
			futureJob.whenDone(hl -> {
				progressIndicator = null;
				setHotspotLog(hl);
				hotspotLogFileConsumer.accept(HotspotLogFile.this);
				futureJob = null;
			});

			futureJob.schedule();
		}

		return progressIndicator;
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
			if (file.exists()) {
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

	@Override
	public IProgress getProgress() {
		return progressIndicator;
	}

}
