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
package io.jitclipse.jitwatch.ui;

import org.eclipse.jface.resource.ImageDescriptor;

import com.link_intersystems.eclipse.ui.jface.resource.ImageRef;

public interface JitWatchUIImages {


	@ImageRef("icons/full/obj16/console.png")
	public ImageDescriptor getConsoleImageDescriptor();

	@ImageRef("platform:/plugin/org.eclipse.ui/icons/full/elcl16/remove.png")
	public ImageDescriptor getRemoveConsoleImageDescriptor();

	@ImageRef("platform:/plugin/org.eclipse.ui/icons/full/elcl16/removeall.png")
	public ImageDescriptor getRemoveAllConsoleImageDescriptor();
}
