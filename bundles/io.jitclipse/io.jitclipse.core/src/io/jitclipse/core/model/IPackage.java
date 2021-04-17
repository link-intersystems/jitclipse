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

import java.util.List;

import com.link_intersystems.eclipse.core.runtime.runtime.IAdaptable2;

public interface IPackage extends IAdaptable2 {

	public String getName();

	default IPackage getRoot() {
		IPackage parentPackage = getParent();
		if (parentPackage == null) {
			return this;
		} else {
			return parentPackage.getRoot();
		}
	}

	public IPackage getParent();

	public List<IPackage> getPackages();

	public List<IClass> getClasses();

	public String getSimpleName();

	default public boolean containsHotspots() {
		for (IClass aClass : getClasses()) {
			IMethodList methods = aClass.getMethods();
			if (methods.containsHotspots()) {
				return true;
			}
		}

		for (IPackage aPackage : getPackages()) {
			if (aPackage.containsHotspots()) {
				return true;
			}
		}

		return false;
	}

}
