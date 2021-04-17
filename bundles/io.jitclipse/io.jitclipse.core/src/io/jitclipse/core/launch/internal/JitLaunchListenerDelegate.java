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
package io.jitclipse.core.launch.internal;

import java.util.ArrayList;
import java.util.List;

import com.link_intersystems.eclipse.core.runtime.runtime.IExtensionPointProxyFactory;

import io.jitclipse.core.launch.IJitLaunch;
import io.jitclipse.core.launch.IJitLaunchListener;

public class JitLaunchListenerDelegate implements IJitLaunchListener {

	private List<IJitLaunchListener> listeners;
	private IExtensionPointProxyFactory extensionPointProxyFactory;

	public JitLaunchListenerDelegate(IExtensionPointProxyFactory extensionPointProxyFactory) {
		this.extensionPointProxyFactory = extensionPointProxyFactory;
	}

	@Override
	public void finished(IJitLaunch jitLaunch) {
		for (IJitLaunchListener listener : getListeners()) {
			listener.finished(jitLaunch);
		}

	}

	private List<IJitLaunchListener> getListeners() {
		if (listeners == null) {
			listeners = new ArrayList<>();
			List<IJitLaunchListenerExtension> jitLaunchExtensions = extensionPointProxyFactory
					.createProxies(IJitLaunchListenerExtension.class);

			for (IJitLaunchListenerExtension iJitLaunchExtension : jitLaunchExtensions) {
				getListeners().add(iJitLaunchExtension.create());
			}
		}
		return listeners;
	}

}
