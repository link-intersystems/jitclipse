package io.jitclipse.core.launch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class EnvTest {

	@Test
	public void parseWinEmptyPath() {
		EnvPath envPath = Env.WINDOWS.parsePath("");
		String formattedPath = Env.WINDOWS.formatPath(envPath);
		assertEquals("", formattedPath);
	}
	@Test
	public void parseWinPath() {
		EnvPath envPath = Env.WINDOWS.parsePath("C:\\somePath;\"D:\\some other path\"");

		assertTrue(envPath.contains("C:\\somePath"));
		assertTrue(envPath.contains("D:\\some other path"));
	}

	@Test
	public void parseLinuxPath() {
		EnvPath envPath = Env.LINUX.parsePath("/home/rene.link:\"/home/some other user\"");

		assertTrue(envPath.contains("/home/rene.link"));
		assertTrue(envPath.contains("/home/some other user"));
	}

	@Test
	void toStringTest() {
		EnvPath envPath = Env.WINDOWS.parsePath("C:\\somePath;\"D:\\some other path\"");
		String formattedPath = Env.WINDOWS.formatPath(envPath);
		assertEquals("C:\\somePath;\"D:\\some other path\"", formattedPath);
	}


	@Test
	public void currentEnv() {
		String classpath = System.getProperty("java.class.path");
		Optional<Env> currentEnv = Env.getCurrent();
		EnvPath envPath = currentEnv.map(env -> env.parsePath(classpath)).get();
		String formattedPath = currentEnv.map(env -> env.formatPath(envPath)).get();
		assertEquals(classpath, formattedPath);
	}
}
