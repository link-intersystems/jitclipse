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
