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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.core.resources.IFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.link_intersystems.eclipse.core.runtime.runtime.IPluginLog;

import io.jitclipse.core.tests.commons.AbstractProjectTest;

class HotspotLogFolderTest extends AbstractProjectTest {

	private IPluginLog pluginLog;
	private HotspotLogFolder hotspotLogFolder;

	@BeforeEach
	public void setupMocks() {
		ZoneId zoneId = ZoneId.of("Europe/Berlin");
		ZonedDateTime zonedDateTime = LocalDateTime.of(2021, 2, 21, 15, 28, 12, 0).atZone(zoneId);
		Clock clock = Clock.fixed(zonedDateTime.toInstant(), zoneId);
		pluginLog = Mockito.mock(IPluginLog.class);
		hotspotLogFolder = new HotspotLogFolder(pluginLog, clock, project);
	}

	@Test
	void newHotspotLogFile() {
		IFile hotspotLogFile = hotspotLogFolder.newHotspotLogFile();
		assertEquals("hotspot-20210221152812.log", hotspotLogFile.getName());
	}

}
