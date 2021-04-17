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
package io.jitclipse.jitwatch.core.parser.internal;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.adoptopenjdk.jitwatch.core.IJITListener;
import org.adoptopenjdk.jitwatch.model.JITEvent;
import org.eclipse.core.resources.IFile;

import com.link_intersystems.eclipse.core.runtime.runtime.IExtensionPointProxyFactory;

import io.jitclipse.jitwatch.core.JitWatchCorePlugin;
import io.jitclipse.jitwatch.core.parser.IParseLogParticipant;

public class ParseLogParticipantBroadcaster implements IParseLogParticipant {

	private static class JITLogListenerBroadcaster implements IJITListener {

		private List<IJITListener> targets;

		public JITLogListenerBroadcaster(List<IJITListener> targetListeners) {
			targets = targetListeners;
		}

		@Override
		public void handleLogEntry(String entry) {
			targets.forEach(t -> t.handleLogEntry(entry));
		}

		@Override
		public void handleErrorEntry(String entry) {
			targets.forEach(t -> t.handleErrorEntry(entry));
		}

		@Override
		public void handleJITEvent(JITEvent event) {
			targets.forEach(t -> t.handleJITEvent(event));
		}

		@Override
		public void handleReadStart() {
			targets.forEach(IJITListener::handleReadStart);
		}

		@Override
		public void handleReadComplete() {
			targets.forEach(IJITListener::handleReadComplete);
		}

	}

	private IExtensionPointProxyFactory proxyFactory;
	private List<IParseLogParticipant> participants;

	public ParseLogParticipantBroadcaster() {
		this(JitWatchCorePlugin.getInstance().getExtensionsPointProxyFactory());

	}

	public ParseLogParticipantBroadcaster(IExtensionPointProxyFactory proxyFactory) {
		this.proxyFactory = proxyFactory;
	}

	private List<IParseLogParticipant> getParseLogParticipants() {
		if (participants == null) {
			List<IParseLogParticipantExtension> proxies = proxyFactory
					.createProxies(IParseLogParticipantExtension.class);
			participants = proxies.stream().map(IParseLogParticipantExtension::createParseLogParticipant)
					.collect(Collectors.toList());
		}
		return participants;
	}

	@Override
	public JITLogListenerBroadcaster aboutToParse(IFile hotspotLogFile) {
		List<IJITListener> targetListeners = getParseLogParticipants().stream()
				.map(pp -> pp.aboutToParse(hotspotLogFile)).filter(Objects::nonNull).collect(Collectors.toList());
		return new JITLogListenerBroadcaster(targetListeners);
	}
}
