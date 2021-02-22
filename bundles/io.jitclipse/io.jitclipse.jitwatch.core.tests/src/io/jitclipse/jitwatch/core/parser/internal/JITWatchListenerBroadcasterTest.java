package io.jitclipse.jitwatch.core.parser.internal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.adoptopenjdk.jitwatch.core.IJITListener;
import org.adoptopenjdk.jitwatch.model.JITEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JITWatchListenerBroadcasterTest {

	private JITWatchListenerBroadcaster broadcaster;
	private IJITListener listener;

	@BeforeEach
	public void setup() {
		broadcaster = new JITWatchListenerBroadcaster();
		listener = mock(IJITListener.class);
		broadcaster.addTarget(listener);
	}

	@Test
	void selfTarget() {
		assertThrows(IllegalArgumentException.class, () -> broadcaster.addTarget(broadcaster));
	}

	@Test
	void handleErrorEntry() {
		broadcaster.handleErrorEntry("entry");

		verify(listener).handleErrorEntry("entry");
	}

	@Test
	void handleJITEvent() {
		JITEvent event = mock(JITEvent.class);
		broadcaster.handleJITEvent(event);

		verify(listener).handleJITEvent(event);
	}

	@Test
	void handleLogEntry() {
		broadcaster.handleLogEntry("entry");

		verify(listener).handleLogEntry("entry");
	}

	@Test
	void handleReadComplete() {
		broadcaster.handleReadComplete();

		verify(listener).handleReadComplete();
	}

	@Test
	void handleReadStart() {
		broadcaster.handleReadStart();

		verify(listener).handleReadStart();
	}

}
