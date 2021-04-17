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
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IPackage;

public class MethodAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, java.lang.Class<T> adapterType) {
		if (IMethod.class.isInstance(adaptableObject)) {
			IMethod method = IMethod.class.cast(adaptableObject);
			return getAdapter(method, adapterType);
		}
		return null;
	}

	public <T> T getAdapter(IMethod method, java.lang.Class<T> adapterType) {
		if (IHotspotLog.class.isAssignableFrom(adapterType)) {
			IClass type = method.getType();
			IPackage packgeObj = type.getPackage();
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
