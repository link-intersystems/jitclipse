package io.jitclipse.core.resources.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.jitclipse.core.JitPluginContext;
import io.jitclipse.core.resources.IJitProject;
import io.jitclipse.core.tests.commons.AbstractJitProjectTest;

class JitProjectTest extends AbstractJitProjectTest {

	@Test
	public void test() {
		List<IJitProject> openJitWatchProjects = IJitProject.getOpenJitWatchProjects();
		assertFalse(openJitWatchProjects.isEmpty());

		openJitWatchProjects = IJitProject.getOpenJitWatchProjects(new IProject[] { project });
		assertEquals(1, openJitWatchProjects.size());
		IJitProject jitProject = openJitWatchProjects.get(0);
		IProject project2 = jitProject.getProject();
		assertEquals(getProjectName(), project2.getName());
	}

	@Test
	public void jitProjectCanBeCreated() {
		JitPluginContext jitPluginContext = Mockito.mock(JitPluginContext.class);
		ZoneId zoneId = ZoneId.of("Europe/Berlin");
		ZonedDateTime zonedDateTime = LocalDateTime.of(2021, 2, 21, 15, 28, 12, 0).atZone(zoneId);
		Clock clock = Clock.fixed(zonedDateTime.toInstant(), zoneId);
		new JitProject(jitPluginContext, clock, project);
	}
}
