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

import java.util.function.Predicate;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdapterFactory;

import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.resources.IHotspotLogFile;

public class FileAdapterFactory implements IAdapterFactory {

	private Predicate<IFile> logFileFilter;

	public FileAdapterFactory() {
		this(IHotspotLogFile::isHotspotLogFile);
	}

	public FileAdapterFactory(Predicate<IFile> logFileFilter) {
		this.logFileFilter = logFileFilter;
	}

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (IFile.class.isInstance(adaptableObject)) {
			IFile file = IFile.class.cast(adaptableObject);
			return getAdapter(file, adapterType);
		}
		return null;
	}

	public <T> T getAdapter(IFile file, Class<T> adapterType) {
		String filename = file.getName() + "." + file.getFileExtension();
		if (IHotspotLogFile.isHotspotLogFilename(filename) && IHotspotLogFile.class.isAssignableFrom(adapterType)) {
			return adapterType.cast(getHotspotLogFileAdapter(file));
		} else if (IHotspotLog.class.equals(adapterType)) {
			IHotspotLogFile hotspotLogFile = getHotspotLogFileAdapter(file);
			if (hotspotLogFile != null) {
				return adapterType.cast(hotspotLogFile.getHotspotLog());
			}
		}
		return null;
	}

	public IHotspotLogFile getHotspotLogFileAdapter(IFile file) {
		if (logFileFilter.test(file)) {
			IHotspotLogFile hotspotLogFile = HotspotLogFile.getForFile(file);
			return hotspotLogFile;
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class<?>[] { IHotspotLogFile.class, IHotspotLog.class };
	}

}
