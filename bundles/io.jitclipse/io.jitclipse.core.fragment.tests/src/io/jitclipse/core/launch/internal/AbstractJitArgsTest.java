package io.jitclipse.core.launch.internal;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class AbstractJitArgsTest extends AbstractJitArgs {

	@Test
	public void isEmptyOptions() {
		assertTrue(isEmpty());
	}

	@Test
	public void setBooleanOption() {
		setBooleanOption("test", true);

		String string = toString();
		assertEquals("-XX:+test", string);
	}

	@Test
	public void setStringOption() {
		setStringOption("test", "value");

		String string = toString();
		assertEquals("-XX:test=\"value\"", string);
	}


	@Test
	public void multipleOptions() {
		setStringOption("opt1", "value1");
		setBooleanOption("opt2", true);
		setStringOption("opt3", "value2");

		String string = toString();
		assertEquals("-XX:opt1=\"value1\" -XX:+opt2 -XX:opt3=\"value2\"", string);
	}

	@Override
	public void setHotspotLogEnabled(boolean enableHostspotLog) {
	}

	@Override
	public void setHotspotLogFile(File hotspotLogFile) {
	}

	@Override
	public void setDisassembledCodeEnabled(boolean disassembledCode) {
	}

	@Override
	public void setClassModelEnabled(boolean classModel) {
	}

}
