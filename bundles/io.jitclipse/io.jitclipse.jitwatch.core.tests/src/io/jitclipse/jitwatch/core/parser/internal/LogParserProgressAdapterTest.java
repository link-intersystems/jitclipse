package io.jitclipse.jitwatch.core.parser.internal;

import static org.eclipse.core.runtime.IProgressMonitor.UNKNOWN;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.adoptopenjdk.jitwatch.model.JITEvent;
import org.adoptopenjdk.jitwatch.parser.ILogParser;
import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LogParserProgressAdapterTest {

	private ILogParser logParser;
	private IProgressMonitor monitor;
	private LogParserProgressAdapter logParserProgressAdapter;

	@BeforeEach
	public void setup() {
		logParser = mock(ILogParser.class);
		monitor = mock(IProgressMonitor.class);
		logParserProgressAdapter = new LogParserProgressAdapter(monitor, logParser);
	}

	@Test
	void handleReadStart() {
		logParserProgressAdapter.handleReadStart();

		verify(monitor).beginTask(Mockito.anyString(), Mockito.eq(UNKNOWN));
	}

	@Test
	void handleReadComplete() {
		logParserProgressAdapter.handleReadComplete();

		verify(monitor).done();
	}

	@Test
	void handleJITEvent() {
		JITEvent event = mock(JITEvent.class);
		logParserProgressAdapter.handleJITEvent(event);

		verify(monitor).setTaskName(Mockito.anyString());
	}

	@Test
	void handleMonitorCanceled() {
		when(monitor.isCanceled()).thenReturn(true);

		logParserProgressAdapter.handleLogEntry("entry");

		verify(logParser).stopParsing();
	}

}
