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
