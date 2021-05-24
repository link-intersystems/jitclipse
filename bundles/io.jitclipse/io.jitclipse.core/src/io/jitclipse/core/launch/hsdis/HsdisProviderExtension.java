package io.jitclipse.core.launch.hsdis;

import com.link_intersystems.eclipse.core.runtime.runtime.ConfigurationElement;
import com.link_intersystems.eclipse.core.runtime.runtime.ExecutableExtension;
import com.link_intersystems.eclipse.core.runtime.runtime.ExtensionPoint;

import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.launch.HsdisProvider;

@ExtensionPoint(namespace = JitCorePlugin.ID, id = "providers")
@ConfigurationElement("hsdisProvider")
public interface HsdisProviderExtension {

	@ExecutableExtension
	public HsdisProvider getProvider();
}
