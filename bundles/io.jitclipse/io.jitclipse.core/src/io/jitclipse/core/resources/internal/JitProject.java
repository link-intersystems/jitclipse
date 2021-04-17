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
package io.jitclipse.core.resources.internal;

import java.time.Clock;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.JitPluginContext;
import io.jitclipse.core.parser.IJitLogParser;
import io.jitclipse.core.resources.IHotspotLogFolder;
import io.jitclipse.core.resources.IJitProject;

public class JitProject implements IJitProject {

	private HotspotLogFolder hotspotLogFileManager;
	private JitPluginContext jitPluginContext;
	private IProject project;

	public JitProject(IProject project) {
		this(JitCorePlugin.getInstance(), Clock.systemDefaultZone(), project);
	}

	public JitProject(JitPluginContext jitPluginContext, Clock clock, IProject project) {
		this.jitPluginContext = jitPluginContext;
		this.project = project;
		hotspotLogFileManager = new HotspotLogFolder(clock, project);
	}

	@Override
	public IHotspotLogFolder getHotspotLogFolder() {
		return hotspotLogFileManager;
	}

	@Override
	public IJitLogParser getParser() throws CoreException {
		return jitPluginContext.getJitLogParser();
	}

	@Override
	public IProject getProject() {
		return project;
	}

}
