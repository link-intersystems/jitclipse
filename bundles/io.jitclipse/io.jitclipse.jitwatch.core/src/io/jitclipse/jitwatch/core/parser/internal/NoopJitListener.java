package io.jitclipse.jitwatch.core.parser.internal;

import org.adoptopenjdk.jitwatch.core.IJITListener;
import org.adoptopenjdk.jitwatch.model.JITEvent;

public class NoopJitListener implements IJITListener {

	public static final IJITListener INSTANCE = new NoopJitListener();

	@Override
	public void handleLogEntry(String entry) {
	}

	@Override
	public void handleErrorEntry(String entry) {
	}

	@Override
	public void handleJITEvent(JITEvent event) {
	}

	@Override
	public void handleReadStart() {
	}

	@Override
	public void handleReadComplete() {
	}

}
