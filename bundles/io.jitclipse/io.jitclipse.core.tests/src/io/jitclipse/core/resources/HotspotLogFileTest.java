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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.jitclipse.core.tests.commons.AbstractJitProjectTest;

@TestMethodOrder(OrderAnnotation.class)
class HotspotLogFileTest extends AbstractJitProjectTest {

	@Test
	void isHotspotLogFilename() {
		assertTrue(IHotspotLogFile.isHotspotLogFilename("hotspot.log"));
		assertFalse(IHotspotLogFile.isHotspotLogFilename("hotspo.log"));
		assertFalse(IHotspotLogFile.isHotspotLogFilename("hotspot.txt"));
	}

}
