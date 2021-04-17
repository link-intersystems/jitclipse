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

import org.eclipse.core.resources.IFile;

import com.link_intersystems.eclipse.core.runtime.runtime.IAdaptable2;

import io.jitclipse.core.model.allocation.IEliminatedAllocationList;
import io.jitclipse.core.model.lock.IOptimisedLockList;
import io.jitclipse.core.model.suggestion.ISuggestionList;

public interface IHotspotLog extends IAdaptable2 {

	public IFile getLogFileLocation();

	public List<IPackage> getRootPackages();

	public boolean contains(IPackage packageObj);

	public IClassList getClasses();

	public ISuggestionList getSuggestionList();

	public IEliminatedAllocationList getEliminatedAllocationList();

	public IOptimisedLockList getOptimizedLockList();

	public ICompilationList getCompilationList();


}
