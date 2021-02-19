package io.jitclipse.core.launch.internal;

import java.util.ArrayList;
import java.util.List;

import com.link_intersystems.eclipse.core.runtime.IExtensionPointProxyFactory;

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
