package io.jitclipse.jitwatch.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;

public final class JitStatus {

	final int code;

	final int severity;

	final String message;

	public JitStatus(int code, int severity, String message) {
		this.code = code;
		this.severity = severity;
		this.message = message;
	}

	public IStatus getStatus() {
		String m = NLS.bind(message, Integer.valueOf(code));
		return new Status(severity, JitWatchCorePlugin.ID, code, m, null);
	}

	public IStatus getStatus(Throwable t) {
		String m = NLS.bind(message, Integer.valueOf(code));
		return new Status(severity, JitWatchCorePlugin.ID, code, m, t);
	}

	public IStatus getStatus(Object param1, Throwable t) {
		String m = NLS.bind(message, Integer.valueOf(code), param1);
		return new Status(severity, JitWatchCorePlugin.ID, code, m, t);
	}

	public IStatus getStatus(Object param1, Object param2, Throwable t) {
		String m = NLS.bind(message, new Object[] { Integer.valueOf(code), param1, param2 });
		return new Status(severity, JitWatchCorePlugin.ID, code, m, t);
	}

	public IStatus getStatus(Object param1) {
		String m = NLS.bind(message, Integer.valueOf(code), param1);
		return new Status(severity, JitWatchCorePlugin.ID, code, m, null);
	}


}
