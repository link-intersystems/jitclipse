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
package io.jitclipse.core.launch;

import java.io.File;

public interface IJitArgs {

	public void setHotspotLogFile(File hotspotLogFile);

	public void setDisassembledCodeEnabled(boolean disassembledCode);

	public void setClassModelEnabled(boolean classModel);

	public String toString();

	public boolean isEmpty();

}
