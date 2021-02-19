package io.jitclipse.jitwatch.ui.console;

import org.adoptopenjdk.jitwatch.core.IJITListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;

import io.jitclipse.jitwatch.core.parser.IParseLogParticipant;

public class ConsoleJITListenerAdapter implements IParseLogParticipant {

	@Override
	public IJITListener aboutToParse(IFile hotspotLogFile) {

		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
		JitWatchMessageConsole messageConsole = new JitWatchMessageConsole(hotspotLogFile);
		messageConsole.setWaterMarks(200000, 400000);

		consoleManager.addConsoles(new IConsole[] { messageConsole });
		consoleManager.showConsoleView(messageConsole);

		return messageConsole;
	}
}
