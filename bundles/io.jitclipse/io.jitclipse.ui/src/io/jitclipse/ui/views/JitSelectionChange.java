package io.jitclipse.ui.views;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IMethod;

public class JitSelectionChange {

	private JitSelection oldSelection;
	private JitSelection newSelection;

	public JitSelectionChange(JitSelection oldSelection, JitSelection newSelection) {
		this.oldSelection = oldSelection;
		this.newSelection = newSelection;
	}

	public <A> void onHotspotLogChanged(BiConsumer<IHotspotLog, A> consumer, A arg) {
		if (isHotspotLogChange()) {
			IHotspotLog hotspotLog = newSelection.getHotspotLog();
			consumer.accept(hotspotLog, arg);
		}
	}

	public void onMethodChanged(Consumer<IMethod> consumer) {
		if (isMethodChange()) {
			consumer.accept(newSelection.getMethod());
		}

	}

	private boolean isMethodChange() {
		return !Objects.equals(oldSelection.getMethod(), newSelection.getMethod());
	}

	private boolean isHotspotLogChange() {
		return !Objects.equals(oldSelection.getHotspotLog(), newSelection.getHotspotLog());
	}

	public boolean isNewSelectionDifferent() {
		return !Objects.equals(oldSelection, newSelection);
	}

}
