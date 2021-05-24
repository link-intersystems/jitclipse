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
package io.jitclipse.core.jdt.launching;

import org.eclipse.jdt.launching.IVMInstall;

import io.jitclipse.core.launch.VMVendor;

public interface IVMVendorDiscoverer {

	public VMVendor discover(IVMInstall vmInstall);
}
