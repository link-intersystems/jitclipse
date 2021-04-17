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
