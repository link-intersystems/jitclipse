package io.jitclipse.core.resources.internal;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.jitclipse.core.JitPluginContext;

class JitProjectTest extends AbstractJitProjectTest {

	@Test
	public void jitProjectCanBeCreated() {
		JitPluginContext jitPluginContext = Mockito.mock(JitPluginContext.class);
		ZoneId zoneId = ZoneId.of("Europe/Berlin");
		ZonedDateTime zonedDateTime = LocalDateTime.of(2021, 2, 21, 15, 28, 12, 0).atZone(zoneId);
		Clock clock = Clock.fixed(zonedDateTime.toInstant(), zoneId);
		new JitProject(jitPluginContext, clock, project);
	}
}
