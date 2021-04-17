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

import org.eclipse.core.runtime.IAdapterFactory;

import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.resources.IHotspotLogFile;

public class HotspotLogAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (IHotspotLogFile.class.isInstance(adaptableObject)) {
			IHotspotLogFile hotspotLogFile = IHotspotLogFile.class.cast(adaptableObject);
			return adapterType.cast(hotspotLogFile.getHotspotLog());
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class<?>[] { IHotspotLog.class };
	}

}
