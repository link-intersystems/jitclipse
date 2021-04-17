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
package io.jitclipse.core.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.junit.jupiter.api.Test;

import io.jitclipse.core.tests.commons.AbstractJitProjectTest;

class AdapterTest extends AbstractJitProjectTest {

	@Test
	void adaptIHotspotLOgFile() {
		List<IFile> files = hotspotLogFolder.getFiles();
		List<IHotspotLogFile> hostpotLogFiles = files.stream().map(f -> f.getAdapter(IHotspotLogFile.class))
				.filter(Objects::nonNull).map(IHotspotLogFile.class::cast).collect(Collectors.toList());

		assertEquals(1, hostpotLogFiles.size());
	}

}
