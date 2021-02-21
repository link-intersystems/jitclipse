package io.jitclipse.core.resources.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.jitclipse.core.resources.IHotspotLogFile;

class HotspotLogFileTest extends AbstractJitProjectTest {

	@Test
	void isHotspotLogFilename() {
		assertTrue(IHotspotLogFile.isHotspotLogFilename("hotspot.log"));
		assertFalse(IHotspotLogFile.isHotspotLogFilename("hotspo.log"));
		assertFalse(IHotspotLogFile.isHotspotLogFilename("hotspot.txt"));
	}

	@Test
	void openHotspotLogFile() throws InterruptedException {
		Optional<IHotspotLogFile> hotspotLogFileOptional = hotspotLogFolder.getHotspotLogFile("hotspot.log");
		assertTrue(hotspotLogFileOptional.isPresent());

		class HotspotLogHolder {

			private CountDownLatch countDownLatch = new CountDownLatch(1);

			private IHotspotLogFile hotspotLog;

			public IHotspotLogFile getHotspotLogFile() throws InterruptedException {
				countDownLatch.await(2, TimeUnit.SECONDS);
				return hotspotLog;
			}

			public void setHotspotLogFile(IHotspotLogFile hotspotLogFile) {
				this.hotspotLog = hotspotLogFile;
				countDownLatch.countDown();
			}

		}

		HotspotLogHolder hotspotLogHolder = new HotspotLogHolder();

		hotspotLogFileOptional.ifPresent(hlf -> hlf.open(hotspotLogHolder::setHotspotLogFile, null));

		IHotspotLogFile hotspotLogFile = hotspotLogHolder.getHotspotLogFile();
		assertNotNull(hotspotLogFile);
		assertTrue(hotspotLogFile.isOpened());
	}

}
