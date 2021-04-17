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
