package io.jitclipse.jitwatch.core.resources;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IClassList;
import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IMethodList;
import io.jitclipse.core.model.IPackage;
import io.jitclipse.core.model.allocation.IEliminatedAllocationList;
import io.jitclipse.core.model.lock.IOptimisedLockList;
import io.jitclipse.core.model.suggestion.ISuggestionList;
import io.jitclipse.core.resources.IHotspotLogFile;
import io.jitclipse.core.tests.commons.AbstractJitProjectTest;
import io.jitclipse.jitwatch.core.parser.IParseLogParticipant;

@TestMethodOrder(OrderAnnotation.class)
class HotspotLogFileTest extends AbstractJitProjectTest {

	private static final String HOTSPOT_LOG_FILENAME = "hotspot-HotspotLogFileTest.log";
	private IParseLogParticipant parseLogParticipant;

	@BeforeEach
	public void setupParseParticipant() throws CoreException {
		addHostspotLogFile(HOTSPOT_LOG_FILENAME);

		parseLogParticipant = Mockito.mock(IParseLogParticipant.class);
		TestParseLogParticipant.delegate = parseLogParticipant;
	}

	@AfterEach
	public void tearDownParseParticipant() {
		TestParseLogParticipant.delegate = null;
		parseLogParticipant = null;
	}

	@Test
	void isHotspotLogFilename() {
		assertTrue(IHotspotLogFile.isHotspotLogFilename("hotspot.log"));
		assertFalse(IHotspotLogFile.isHotspotLogFilename("hotspo.log"));
		assertFalse(IHotspotLogFile.isHotspotLogFilename("hotspot.txt"));
	}

	@Test
	void parseLogParticipant() throws InterruptedException {
		doOpenHotspotLogFile();
		Mockito.verify(parseLogParticipant).aboutToParse(Mockito.any(IFile.class));
	}

	@Test
	void openHotspotLogFile() throws InterruptedException {
		IHotspotLogFile hotspotLogFile = doOpenHotspotLogFile();
		assertNotNull(hotspotLogFile);
		assertTrue(hotspotLogFile.isOpened());

		IHotspotLog hotspotLog = hotspotLogFile.getHotspotLog();
		assertNotNull(hotspotLog);
	}

	@Test
	void adapterTest() throws InterruptedException {
		IHotspotLogFile hotspotLogFile = doOpenHotspotLogFile();

		assertNotNull(hotspotLogFile);
		assertTrue(hotspotLogFile.isOpened());

		IHotspotLog hotspotLog = hotspotLogFile.getAdapter(IHotspotLog.class);
		assertNotNull("adapt IHotspotLogFile to HotspotLog", hotspotLog);

		List<IPackage> rootPackages = hotspotLog.getRootPackages();
		for (IPackage rootPackage : rootPackages) {
			adapterTest(rootPackage);
		}
	}

	private void adapterTest(IPackage aPackage) {
		IHotspotLog hotspotLog = aPackage.getAdapter(IHotspotLog.class);
		assertNotNull("adapt " + aPackage.getName() + " to HotspotLog", hotspotLog);

		List<IPackage> packages = aPackage.getPackages();
		for (IPackage aSubPackage : packages) {
			List<IClass> classes = aSubPackage.getClasses();
			for (IClass aClass : classes) {
				adapterTest(aClass);
			}
			adapterTest(aSubPackage);
		}

	}

	private void adapterTest(IClass aClass) {
		IHotspotLog hotspotLog = aClass.getAdapter(IHotspotLog.class);
		assertNotNull("adapt " + aClass.getName() + " to HotspotLog", hotspotLog);

		IMethodList methods = aClass.getMethods();
		for (IMethod aMethod : methods) {
			adapterTest(aMethod);
		}
	}

	private void adapterTest(IMethod aMethod) {
		IHotspotLog hotspotLog = aMethod.getAdapter(IHotspotLog.class);
		assertNotNull("adapt " + aMethod.getName() + " to HotspotLog", hotspotLog);
	}

	private IHotspotLogFile doOpenHotspotLogFile() throws InterruptedException {
		Optional<IHotspotLogFile> hotspotLogFileOptional = hotspotLogFolder.getHotspotLogFile(HOTSPOT_LOG_FILENAME);
		assertTrue(hotspotLogFileOptional.isPresent());

		class HotspotLogHolder {

			private CountDownLatch countDownLatch = new CountDownLatch(1);

			private IHotspotLogFile hotspotLog;

			public IHotspotLogFile getHotspotLogFile() throws InterruptedException {
				countDownLatch.await(5, SECONDS);
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
	void hostspotLogElements() throws InterruptedException, CoreException {
		IHotspotLogFile hotspotLogFile = doOpenHotspotLogFile();
		IHotspotLog hotspotLog = hotspotLogFile.getHotspotLog();
		assertNotNull(hotspotLog);

		IClassList classes = hotspotLog.getClasses();
		assertNotNull(classes);

		Optional<IClass> mathClassOptional = classes.findClassByName("java.lang.Math");
		assertTrue(mathClassOptional.isPresent());
		IClass mathClass = mathClassOptional.get();

		IMethodList methods = mathClass.getMethods();
		assertNotNull(methods);

		Optional<IMethod> floorMethodOptional = methods.findByName("floor");
		assertTrue(floorMethodOptional.isPresent());
		IMethod floorMethod = floorMethodOptional.get();
		assertEquals("floor(double)", floorMethod.toSignatureString());

		List<ICompilation> compilations = hotspotLog.getCompilationList();
		assertNotNull(compilations);

		ISuggestionList suggestionList = hotspotLog.getSuggestionList();
		assertNotNull(suggestionList);

		IOptimisedLockList optimizedLockList = hotspotLog.getOptimizedLockList();
		assertNotNull(optimizedLockList);

		IEliminatedAllocationList eliminatedAllocationList = hotspotLog.getEliminatedAllocationList();
		assertNotNull(eliminatedAllocationList);
	}

}
