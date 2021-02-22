package io.jitclipse.jitwatch.core.resources;

import org.adoptopenjdk.jitwatch.core.IJITListener;
import org.eclipse.core.resources.IFile;

import io.jitclipse.jitwatch.core.parser.IParseLogParticipant;

public class TestParseLogParticipant implements IParseLogParticipant {

	public static IParseLogParticipant delegate;

	public TestParseLogParticipant() {
	}

	@Override
	public IJITListener aboutToParse(IFile hotspotLogFile) {
		if(delegate != null) {
			return delegate.aboutToParse(hotspotLogFile);
		}
		return null;
	}

}
