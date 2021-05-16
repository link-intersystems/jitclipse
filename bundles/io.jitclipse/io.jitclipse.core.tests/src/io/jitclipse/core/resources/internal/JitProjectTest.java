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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
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
	public void getOpenJitWatchProjects() {
		List<IJitProject> openJitWatchProjects = IJitProject.getOpenJitWatchProjects();
		assertEquals(1, openJitWatchProjects.size());

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

	@Test
	public void jitProjectSourceLocations() {
		List<String> sourceLocations = jitProject.getSourceLocations();

		assertEquals(1, sourceLocations.size()); // src

		String projectLocation = jitProject.getProject().getLocation().toOSString();

		assertLocationExists(sourceLocations, projectLocation, "src");
	}

	@Test
	public void jitProjectBinaryResourceLocations() {
		List<String> binaryResourceLocations = jitProject.getBinaryResourceLocations();

		assertEquals(3, binaryResourceLocations.size()); // bin, libs, JRE rt.jar

		String projectLocation = jitProject.getProject().getLocation().toOSString();

		assertLocationExists(binaryResourceLocations, projectLocation, "libs", "commons-lang.jar");
		assertLocationExists(binaryResourceLocations, projectLocation, "bin");
	}

	private void assertLocationExists(List<String> locations, String... pathSegments) {
		String expectedOsLocation = osLocationString(pathSegments);
		assertTrue(expectedOsLocation + " exists", locations.contains(expectedOsLocation));
	}

	private String osLocationString(String... pathSegments) {
		return String.join(File.separator, pathSegments);
	}
}
