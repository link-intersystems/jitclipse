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
import java.util.Optional;
import java.util.function.Predicate;

public interface IMethodList extends List<IMethod> {

	default public boolean containsHotspots() {
		for (IMethod method : this) {
			if (method.isHot()) {
				return true;
			}
		}
		return false;
	}

	Optional<IMethod> findByName(String name);

	Optional<IMethod> find(Predicate<IMethod> predicate);

}
