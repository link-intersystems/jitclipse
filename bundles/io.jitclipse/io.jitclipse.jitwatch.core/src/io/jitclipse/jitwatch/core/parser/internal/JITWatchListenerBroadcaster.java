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

import java.util.ArrayList;
import java.util.List;

import org.adoptopenjdk.jitwatch.core.IJITListener;
import org.adoptopenjdk.jitwatch.model.JITEvent;

public class JITWatchListenerBroadcaster implements IJITListener {

	private List<IJITListener> targets = new ArrayList<>();

	public void handleLogEntry(String entry) {
		targets.forEach(l -> l.handleLogEntry(entry));
	}

	public void handleErrorEntry(String entry) {
		targets.forEach(l -> l.handleErrorEntry(entry));
	}

	public void handleJITEvent(JITEvent event) {
		targets.forEach(l -> l.handleJITEvent(event));
	}

	public void handleReadStart() {
		targets.forEach(IJITListener::handleReadStart);
	}

	public void handleReadComplete() {
		targets.forEach(IJITListener::handleReadComplete);
	}

	public void addTarget(IJITListener target) {
		if (target == this) {
			throw new IllegalArgumentException("Can't add myself to the broadcast targets.");
		}
		this.targets.add(target);
	}

}
