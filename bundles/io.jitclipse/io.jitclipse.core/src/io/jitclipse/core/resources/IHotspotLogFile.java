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
package io.jitclipse.core.resources;

import java.util.function.Consumer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import com.link_intersystems.beans.PropertyChangeSource;
import com.link_intersystems.eclipse.core.runtime.runtime.IAdaptable2;
import com.link_intersystems.eclipse.core.runtime.runtime.jobs.FutureJob;
import com.link_intersystems.eclipse.core.runtime.runtime.progress.IProgress;

import io.jitclipse.core.model.IHotspotLog;

public interface IHotspotLogFile extends IAdaptable2, PropertyChangeSource {

	public static final String PROPERTY_HOTSPOT_LOG = "hotspotLog";

	public static boolean isHotspotLogFile(IFile file) {
		return isHotspotLogFilename(file.getName());
	}

	public static boolean isHotspotLogFilename(String name) {
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
