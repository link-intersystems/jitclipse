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

import java.util.ArrayList;
import java.util.List;

public class TestPackage implements IPackage {

	private TestPackage parent;
	private List<IClass> classes = new ArrayList<>();
	private List<IPackage> packages = new ArrayList<>();

	public TestPackage() {
		this(null);
	}

	public TestPackage(TestPackage parent) {
		this.parent = parent;
		if (this.parent != null) {
			this.parent.addPackage(this);
		}
	}

	private void addPackage(TestPackage testPackage) {
		packages.add(testPackage);
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public IPackage getParent() {
		return parent;
	}

	@Override
	public List<IPackage> getPackages() {
		return packages;
	}

	@Override
	public List<IClass> getClasses() {
		return classes;
	}

	@Override
	public String getSimpleName() {
		return null;
	}

	public void addClass(IClass aClass) {
		classes.add(aClass);
	}

}