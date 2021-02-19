package io.jitclipse.jitwatch.core.parser.internal;

import org.adoptopenjdk.jitwatch.core.IJITListener;
import org.adoptopenjdk.jitwatch.model.JITEvent;
import org.adoptopenjdk.jitwatch.parser.ILogParser;
import org.eclipse.core.runtime.IProgressMonitor;

public class LogParserProgressAdapter implements IJITListener {

	private IProgressMonitor monitor;
	private ILogParser logParser;

	public LogParserProgressAdapter(IProgressMonitor monitor, ILogParser logParser) {
		this.monitor = monitor;
		this.logParser = logParser;
	}

	@Override
	public void handleLogEntry(String entry) {
		checkCancel();
	}

	@Override
	public void handleErrorEntry(String entry) {
		checkCancel();
	}

	@Override
	public void handleJITEvent(JITEvent event) {
		checkCancel();

		monitor.setTaskName(event.toString());
	}

	private void checkCancel() {
		if (monitor.isCanceled()) {
			logParser.stopParsing();
		}
	}

	@Override
	public void handleReadStart() {
		checkCancel();
		monitor.beginTask("Parsing", IProgressMonitor.UNKNOWN);
	}

	@Override
	public void handleReadComplete() {
		monitor.done();
	}

}
