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
package io.jitclipse.ui.views.locks;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.link_intersystems.eclipse.ui.jface.viewers.AdaptableSelectionList;

import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.lock.IOptimisedLock;
import io.jitclipse.core.model.lock.IOptimisedLockList;
import io.jitclipse.ui.views.AbstractHotspotView;

public class OptimizedLocksView extends AbstractHotspotView<IOptimisedLock> {

	public static final String ID = "io.jitclipse.ui.views.OptimizedLockView";

	@Override
	protected StructuredViewer createViewer(Composite parent) {
		return new OptimizedLocksViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
	}

	@Override
	protected Object toHotspotViewElement(Object viewerElement) {
		IOptimisedLock optimisedLock = (IOptimisedLock) viewerElement;
		return optimisedLock.getMethod();
	}

	@Override
	protected String getHelpId() {
		return "io.jitclipse.ui.help.optimizedLock.viewer";
	}

	@Override
	protected String selectionToString(ISelection selection) {
		List<IOptimisedLock> optimisedLocks = new AdaptableSelectionList<>(IOptimisedLock.class, selection);
		return toString(optimisedLocks);
	}

	private String toString(List<IOptimisedLock> optimisedLocks) {
		if (optimisedLocks.isEmpty()) {
			return "";
		}

		IOptimisedLock optimisedLock = optimisedLocks.get(0);

		return optimisedLock.getMethod().getType().getName();
	}

	@Override
	protected IMethod toMethod(IOptimisedLock element) {
		return element.getMethod();
	}

	@Override
	protected void updateViewer(Viewer viewer, IHotspotLog hotspotLog) {
		IOptimisedLockList optimisedLockList = null;

		if (hotspotLog != null) {
			optimisedLockList = hotspotLog.getOptimizedLockList();
		}

		viewer.setInput(optimisedLockList);
	}

}
