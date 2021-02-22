package io.jitclipse.core.resources.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.CoreException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.allocation.IEliminatedAllocationList;
import io.jitclipse.core.model.lock.IOptimisedLockList;
import io.jitclipse.core.model.suggestion.ISuggestionList;
import io.jitclipse.core.resources.IHotspotLogFile;

@TestMethodOrder(OrderAnnotation.class)
class HotspotLogFileTest extends AbstractJitProjectTest {

	@Test
	void isHotspotLogFilename() {
		assertTrue(IHotspotLogFile.isHotspotLogFilename("hotspot.log"));
		assertFalse(IHotspotLogFile.isHotspotLogFilename("hotspo.log"));
		assertFalse(IHotspotLogFile.isHotspotLogFilename("hotspot.txt"));
	}

	@Test
	void openHotspotLogFile() throws InterruptedException {
		IHotspotLogFile hotspotLogFile = doOpenHotspotLogFile();
		assertNotNull(hotspotLogFile);
		assertTrue(hotspotLogFile.isOpened());

		IHotspotLog hotspotLog = hotspotLogFile.getHotspotLog();
		assertNotNull(hotspotLog);
	}

	private IHotspotLogFile doOpenHotspotLogFile() throws InterruptedException {
		Optional<IHotspotLogFile> hotspotLogFileOptional = hotspotLogFolder.getHotspotLogFile("hotspot.log");
		assertTrue(hotspotLogFileOptional.isPresent());

		class HotspotLogHolder {

			private CountDownLatch countDownLatch = new CountDownLatch(1);

			private IHotspotLogFile hotspotLog;

			public IHotspotLogFile getHotspotLogFile() throws InterruptedException {
				countDownLatch.await(5, TimeUnit.SECONDS);
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
		return hotspotLogFile;
	}

	@Test
	void getHotspotLog() throws InterruptedException, CoreException {
		IHotspotLogFile hotspotLogFile = doOpenHotspotLogFile();
		IHotspotLog hotspotLog = hotspotLogFile.getHotspotLog();
		assertNotNull(hotspotLog);

		List<IClass> classes = hotspotLog.getClasses();
		assertNotNull(classes);

		ISuggestionList suggestionList = hotspotLog.getSuggestionList();
		assertNotNull(suggestionList);

		IOptimisedLockList optimizedLockList = hotspotLog.getOptimizedLockList();
		assertNotNull(optimizedLockList);

		IEliminatedAllocationList eliminatedAllocationList = hotspotLog.getEliminatedAllocationList();
		assertNotNull(eliminatedAllocationList);
	}

}
