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

import com.link_intersystems.eclipse.core.runtime.runtime.IAdaptable2;

public interface IMethod extends IAdaptable2 {

	public String getName();

	public String toSignatureString();

	public String[] toParameterSignatureStrings();

	public IClass getType();

	boolean matches(String fullyQualifiedName);

	public IMemberByteCode getMemberByteCode();

	public List<ICompilation> getCompilations();

	default public Optional<ICompilation> getLatestCompilation() {
		return getCompilations().stream().reduce((first, second) -> second);
	}

	default boolean isHot() {
		return !getCompilations().isEmpty();
	}

}
