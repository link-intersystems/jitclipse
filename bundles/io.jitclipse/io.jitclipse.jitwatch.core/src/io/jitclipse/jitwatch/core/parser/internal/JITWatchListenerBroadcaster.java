package io.jitclipse.jitwatch.core.parser.internal;

import java.util.ArrayList;
import java.util.List;

import org.adoptopenjdk.jitwatch.core.IJITListener;
import org.adoptopenjdk.jitwatch.model.JITEvent;

public class JITWatchListenerBroadcaster implements IJITListener {

	private List<IJITListener> targets = new ArrayList<>();

	public void handleLogEntry(String entry) {
		targets.forEach(l -> l.handleLogEntry(entry));
	}

	public void handleErrorEntry(String entry) {
		targets.forEach(l -> l.handleErrorEntry(entry));
	}

	public void handleJITEvent(JITEvent event) {
		targets.forEach(l -> l.handleJITEvent(event));
	}

	public void handleReadStart() {
		targets.forEach(IJITListener::handleReadStart);
	}

	public void handleReadComplete() {
		targets.forEach(IJITListener::handleReadComplete);
	}

	public void addTarget(IJITListener target) {
		if (target == this) {
			throw new IllegalArgumentException("Can't add myself to the broadcast targets.");
		}
		this.targets.add(target);
	}

}
