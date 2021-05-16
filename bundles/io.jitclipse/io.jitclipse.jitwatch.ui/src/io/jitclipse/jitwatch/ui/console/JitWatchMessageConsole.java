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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.adoptopenjdk.jitwatch.core.IJITListener;
import org.adoptopenjdk.jitwatch.model.JITEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import io.jitclipse.jitwatch.ui.JitWatchUIImages;
import io.jitclipse.jitwatch.ui.JitWatchUIPlugin;

public class JitWatchMessageConsole extends MessageConsole implements IJITListener {

	public static void removeAll() {
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();

		List<IConsole> removeConsoles = new ArrayList<>();

		IConsole[] consoles = consoleManager.getConsoles();
		for (IConsole console : consoles) {
			if (console instanceof JitWatchMessageConsole) {
				removeConsoles.add(console);
			}
		}

		consoleManager.removeConsoles(removeConsoles.toArray(new IConsole[0]));
	}

	private MessageConsoleStream messageStream;

	public JitWatchMessageConsole(IFile hotspotLogFile) {
		this(hotspotLogFile, JitWatchUIPlugin.getInstance().getJitUIImages());
	}

	public JitWatchMessageConsole(IFile hotspotLogFile, JitWatchUIImages jitWatchUIImages) {
		super("Hotspot Parser Log [" + hotspotLogFile.getName() + "]", jitWatchUIImages.getConsoleImageDescriptor());
	}

	public void remove() {
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
		consoleManager.removeConsoles(new IConsole[] { this });
	}

	@Override
	public void handleLogEntry(String entry) {
		getMessageStream().println(entry);
	}

	@Override
	public void handleErrorEntry(String entry) {
		getMessageStream().println("[ERROR] - " + entry);
	}

	@Override
	public void handleJITEvent(JITEvent event) {
		String formatted = MessageFormat.format("[{1}] - {0} - {2}", event.getStamp(), event.getEventType(),
				event.getEventMember());
		getMessageStream().println(formatted);
	}

	@Override
	public void handleReadStart() {
		clearConsole();
		messageStream = newMessageStream();
	}

	@Override
	public void handleReadComplete() {
		closeMessageStream();
	}

	@Override
	protected void dispose() {
		super.dispose();
		closeMessageStream();
	}

	private void closeMessageStream() {
		if (messageStream != null) {
			try {
				messageStream.close();
			} catch (IOException e) {
			}
			messageStream = null;
		}
	}

	private MessageConsoleStream getMessageStream() {
		if (messageStream == null) {
			messageStream = newMessageStream();
		}
		return messageStream;
	}

}
