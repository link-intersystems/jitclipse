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
package io.jitclipse.jitwatch.core.model;

import java.util.List;

import org.adoptopenjdk.jitwatch.model.MetaPackage;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IPackage;

public class Package implements IPackage {

	private MetaPackage metaPackage;
	private ModelContext modelContext;

	public Package(ModelContext modelContext, MetaPackage metaPackage) {
		this.modelContext = modelContext;
		this.metaPackage = metaPackage;
	}

	@Override
	public String getName() {
		return metaPackage.getName();
	}

	@Override
	public IPackage getParent() {
		return modelContext.getPackage(metaPackage.getParentPackage());
	}

	@Override
	public List<IPackage> getPackages() {
		return modelContext.getPackages(metaPackage.getChildPackages());
	}

	@Override
	public List<IClass> getClasses() {
		return modelContext.getClasses(metaPackage.getPackageClasses());
	}

	@Override
	public String getSimpleName() {
		String name = metaPackage.getName();

		MetaPackage parentPackage = metaPackage.getParentPackage();
		if (parentPackage != null) {
			name = name.substring(parentPackage.getName().length() + 1);
		}

		return name;
	}

}
