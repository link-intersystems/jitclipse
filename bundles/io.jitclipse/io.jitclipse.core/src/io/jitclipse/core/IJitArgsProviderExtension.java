package io.jitclipse.core;

import com.link_intersystems.eclipse.core.runtime.runtime.ConfigurationElement;
import com.link_intersystems.eclipse.core.runtime.runtime.ExecutableExtension;
import com.link_intersystems.eclipse.core.runtime.runtime.ExtensionPoint;

import io.jitclipse.core.launch.IJitArgsProvider;

@ExtensionPoint(namespace = JitCorePlugin.ID, id = "launch")
@ConfigurationElement("argsProvider")
public interface IJitArgsProviderExtension {

	@ExecutableExtension
	public IJitArgsProvider createArgsProvider();

}
