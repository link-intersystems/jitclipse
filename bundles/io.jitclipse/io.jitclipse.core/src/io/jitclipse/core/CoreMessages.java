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

}
