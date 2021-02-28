package io.jitclipse.core.launch.internal;

import static java.text.MessageFormat.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.link_intersystems.eclipse.core.runtime.IPluginLog;

class ExecutionEnvironmentVersionComparatorTest {

	private static final int GREATER = 1;
	private static final int EQUAL = 0;
	private static final int LOWER = -1;

	private IPluginLog pluginLog;
	private ExecutionEnvironmentVersionComparator comparator;
	private IExecutionEnvironment env1;
	private IExecutionEnvironment env2;

	@BeforeEach
	public void setup() {
		pluginLog = Mockito.mock(IPluginLog.class);
		comparator = new ExecutionEnvironmentVersionComparator(pluginLog);

		env1 = Mockito.mock(IExecutionEnvironment.class);
		env2 = Mockito.mock(IExecutionEnvironment.class);
	}

	private void setEnv1(String id) {
		when(env1.getId()).thenReturn(id);
	}

	private void setEnv2(String id) {
		when(env2.getId()).thenReturn(id);
	}

	private void asssertThatVersion(String env1Id, int expectedResult, String env2Id) {
		setEnv1(env1Id);
		setEnv2(env2Id);

		int order = comparator.compare(env1, env2);
		assertEquals(expectedResult, order, () -> format("{0} <> {1} = {2}", env1Id, env2Id, order));
	}

	@Test
	void equalVersions() {
		asssertThatVersion("JRE-1.1", EQUAL, "JRE-1.1");
		asssertThatVersion("J2SE-1.4", EQUAL, "J2SE-1.4");
		asssertThatVersion("JavaSE-1.8", EQUAL, "JavaSE-1.8");
		asssertThatVersion("JavaSE-11", EQUAL, "JavaSE-11");
	}

	@Test
	void greaterVersions() {
		asssertThatVersion("J2SE-1.4", GREATER, "JRE-1.1");
		asssertThatVersion("JavaSE-1.8", GREATER, "JRE-1.1");
		asssertThatVersion("JavaSE-11", GREATER, "JRE-1.1");

		asssertThatVersion("JavaSE-11", GREATER, "JavaSE-1.8");

		asssertThatVersion("JavaSE-10", GREATER, "JavaSE-9");
		asssertThatVersion("JavaSE-15", GREATER, "JavaSE-11");
	}

	@Test
	void lowerVersions() {
		asssertThatVersion("JRE-1.1", LOWER, "J2SE-1.4");
		asssertThatVersion("JRE-1.1", LOWER, "JavaSE-1.8");
		asssertThatVersion("JRE-1.1", LOWER, "JavaSE-11");

		asssertThatVersion("J2SE-1.4", LOWER, "JavaSE-1.8");
		asssertThatVersion("J2SE-1.4", LOWER, "JavaSE-11");

		asssertThatVersion("JavaSE-11", LOWER, "JavaSE-15");
	}

	@Test
	void parseExceptionNoVersion() {
		asssertThatVersion("JRE", EQUAL, "JAVA");

		verify(pluginLog).logError(Mockito.anyString(), Mockito.any(Exception.class));
	}

	@Test
	void parseExceptionWrongVersion() {
		asssertThatVersion("JRE-A", EQUAL, "JAVA-B");

		verify(pluginLog).logError(Mockito.anyString(), Mockito.any(Exception.class));
	}

}
