package io.jitclipse.core.parser.internal;

import com.link_intersystems.eclipse.core.runtime.runtime.ConfigurationElement;
import com.link_intersystems.eclipse.core.runtime.runtime.ExecutableExtension;
import com.link_intersystems.eclipse.core.runtime.runtime.ExtensionPoint;

import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.parser.IJitLogParser;

@ExtensionPoint(namespace = JitCorePlugin.ID, id = "parsers")
@ConfigurationElement("parser")
public interface IJitParserExtension {

	@ExecutableExtension("class")
	public IJitLogParser createParser();

}
