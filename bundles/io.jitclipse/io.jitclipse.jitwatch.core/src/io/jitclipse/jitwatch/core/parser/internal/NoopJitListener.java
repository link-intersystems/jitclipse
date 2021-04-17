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

import org.adoptopenjdk.jitwatch.core.IJITListener;
import org.adoptopenjdk.jitwatch.model.JITEvent;

public class NoopJitListener implements IJITListener {

	public static final IJITListener INSTANCE = new NoopJitListener();

	@Override
	public void handleLogEntry(String entry) {
	}

	@Override
	public void handleErrorEntry(String entry) {
	}

	@Override
	public void handleJITEvent(JITEvent event) {
	}

	@Override
	public void handleReadStart() {
	}

	@Override
	public void handleReadComplete() {
	}

}
