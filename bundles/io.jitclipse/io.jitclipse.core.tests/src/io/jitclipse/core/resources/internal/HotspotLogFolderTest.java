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
