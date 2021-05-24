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
package io.jitclipse.jitwatch.core.launch;

import java.io.File;

import io.jitclipse.core.launch.AbstractJitArgs;

public class OpenJDKJitArgs extends AbstractJitArgs {

	@Override
	public void setHotspotLogFile(File hotspotLogFile) {
		setHotspotLogEnabled();
		setStringOption("LogFile", hotspotLogFile.toString());
		setDisassembledCodeEnabled(true);
	}

	private void setHotspotLogEnabled() {
		setBooleanOption("UnlockDiagnosticVMOptions", true);
		setBooleanOption("TraceClassLoading", true);
		setBooleanOption("LogCompilation", true);
	}

	@Override
	public void setDisassembledCodeEnabled(boolean disassembledCode) {
		setBooleanOption("PrintAssembly", disassembledCode);
	}

	@Override
	public void setClassModelEnabled(boolean classModel) {
		setBooleanOption("TraceClassLoading ", classModel);
	}

}
