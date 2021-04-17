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
package io.jitclipse.core.model.internal;

import org.eclipse.core.runtime.IAdapterFactory;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IPackage;

public class ClassAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, java.lang.Class<T> adapterType) {
		if (IClass.class.isInstance(adaptableObject)) {
			IClass clazz = IClass.class.cast(adaptableObject);
			return getAdapter(clazz, adapterType);
		}
		return null;
	}

	public <T> T getAdapter(IClass clazz, java.lang.Class<T> adapterType) {
		if (IHotspotLog.class.isAssignableFrom(adapterType)) {
			IPackage packgeObj = clazz.getPackage();
			IPackage rootPackage = packgeObj.getRoot();
			return adapterType.cast(rootPackage.getAdapter(adapterType));
		}
		return null;

	}

	@Override
	public java.lang.Class<?>[] getAdapterList() {
		return new java.lang.Class<?>[] { IHotspotLog.class };
	}

}
