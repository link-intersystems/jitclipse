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
package io.jitclipse.core;

import org.eclipse.osgi.util.NLS;

public class CoreMessages extends NLS {

	private static final String BUNDLE_NAME = "io.jitclipse.core.coremessages";//$NON-NLS-1$

	static {
		NLS.initializeMessages(BUNDLE_NAME, CoreMessages.class);
	}

	public static String Launching_task;
	public static String UNKOWN_LAUNCH_TYPE_ERROR;
	public static String NO_JIT_LOG_PARSER_AVAILABLE;
	public static String NO_JIT_ARGS_PROVIDER_AVAILABLE;

}
