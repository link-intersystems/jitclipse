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
package io.jitclipse.core.launch.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import io.jitclipse.core.launch.AbstractJitArgs;

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
	public void setHotspotLogFile(File hotspotLogFile) {
	}

	@Override
	public void setDisassembledCodeEnabled(boolean disassembledCode) {
	}

	@Override
	public void setClassModelEnabled(boolean classModel) {
	}

}
