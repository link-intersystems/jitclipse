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
package io.jitclipse.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.model.WorkbenchContentProvider;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IPackage;

public class JitModelContentProvider extends WorkbenchContentProvider {

	@Override
	public Object[] getChildren(Object element) {
		List<Object> children = new ArrayList<>();

		if (IPackage.class.isInstance(element)) {
			IPackage packageObj = IPackage.class.cast(element);
			List<IClass> packageClasses = packageObj.getClasses();
			children.addAll(packageClasses);

			List<IPackage> childPackages = packageObj.getPackages();
			children.addAll(childPackages);
		} else if (IClass.class.isInstance(element)) {
			IClass clazz = IClass.class.cast(element);
			List<IMethod> methods = clazz.getMethods();
			children.addAll(methods);
		}

		return children.toArray();
	}
}
