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
