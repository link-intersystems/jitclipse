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
package io.jitclipse.core.model;

import com.link_intersystems.eclipse.core.runtime.runtime.IAdaptable2;

public interface IClass extends IAdaptable2 {

	public String getName();

	public IPackage getPackage();

	public IMethodList getMethods();

	public String getSimpleName();

	public IClassByteCode getByteCode();

	default public boolean containsHotspots() {
		return getMethods().containsHotspots();
	}

}
