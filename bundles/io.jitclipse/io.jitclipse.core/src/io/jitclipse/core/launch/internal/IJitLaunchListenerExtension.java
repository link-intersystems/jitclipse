package io.jitclipse.core.launch.internal;

import com.link_intersystems.eclipse.core.runtime.runtime.ConfigurationElement;
import com.link_intersystems.eclipse.core.runtime.runtime.ExecutableExtension;
import com.link_intersystems.eclipse.core.runtime.runtime.ExtensionPoint;

import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.launch.IJitLaunchListener;

@ExtensionPoint(namespace = JitCorePlugin.ID, id = "listeners")
@ConfigurationElement(value = "launchListener")
public interface IJitLaunchListenerExtension {

	@ExecutableExtension(value = "class")
	public IJitLaunchListener create();
}
