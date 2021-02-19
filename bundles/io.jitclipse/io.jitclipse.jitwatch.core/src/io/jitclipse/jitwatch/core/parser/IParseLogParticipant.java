package io.jitclipse.jitwatch.core.parser;

import org.adoptopenjdk.jitwatch.core.IJITListener;
import org.eclipse.core.resources.IFile;

public interface IParseLogParticipant {

	IJITListener aboutToParse(IFile hotspotLogFile);

}
