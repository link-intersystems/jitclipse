package io.jitclipse.jitwatch.core.parser.internal;

import com.link_intersystems.eclipse.core.runtime.runtime.ConfigurationElement;
import com.link_intersystems.eclipse.core.runtime.runtime.ExecutableExtension;
import com.link_intersystems.eclipse.core.runtime.runtime.ExtensionPoint;

import io.jitclipse.jitwatch.core.JitWatchCorePlugin;
import io.jitclipse.jitwatch.core.parser.IParseLogParticipant;

@ExtensionPoint(namespace = JitWatchCorePlugin.ID, id = "listeners")
@ConfigurationElement("parserLogParticipant")
public interface IParseLogParticipantExtension {

	@ExecutableExtension
	public IParseLogParticipant createParseLogParticipant();
}
