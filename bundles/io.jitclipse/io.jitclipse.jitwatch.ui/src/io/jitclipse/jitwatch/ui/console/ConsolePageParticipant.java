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
package io.jitclipse.jitwatch.ui.console;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;

import io.jitclipse.jitwatch.ui.JitWatchUIImages;
import io.jitclipse.jitwatch.ui.JitWatchUIPlugin;

public class ConsolePageParticipant implements IConsolePageParticipant {

	private static final String REMOVE_ALL_ACTION_ID = "io.jitclipse.jitwatch.ui.console.removeAll";
	private static final String REMOVE_ACTION_ID = "io.jitclipse.jitwatch.ui.console.remove";

	private Action removeAction;
	private Action removeAllAction;

	private JitWatchUIImages jitWatchUIImages;
	private IActionBars actionBars;
	private JitWatchMessageConsole jitWatchMessageConsole;

	public ConsolePageParticipant() {
		this(JitWatchUIPlugin.getInstance().getJitUIImages());

	}

	public ConsolePageParticipant(JitWatchUIImages jitWatchUIImages) {
		this.jitWatchUIImages = jitWatchUIImages;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public void init(IPageBookViewPage page, IConsole console) {
		if (console instanceof JitWatchMessageConsole) {
			jitWatchMessageConsole = (JitWatchMessageConsole) console;

			actionBars = page.getSite().getActionBars();

			removeAction = new Action("Remove", jitWatchUIImages.getRemoveConsoleImageDescriptor()) {
				@Override
				public void run() {
					JitWatchMessageConsole jitWatchMessageConsole = (JitWatchMessageConsole) console;
					jitWatchMessageConsole.remove();
				}
			};
			removeAction.setId(REMOVE_ACTION_ID);

			removeAllAction = new Action("Remove All", jitWatchUIImages.getRemoveAllConsoleImageDescriptor()) {
				@Override
				public void run() {
					JitWatchMessageConsole.removeAll();
				}
			};
			removeAction.setId(REMOVE_ALL_ACTION_ID);

			configureToolBar(actionBars.getToolBarManager());
		}
	}

	protected void configureToolBar(IToolBarManager mgr) {
		mgr.appendToGroup(IConsoleConstants.LAUNCH_GROUP, removeAction);
		mgr.appendToGroup(IConsoleConstants.LAUNCH_GROUP, removeAllAction);
	}

	@Override
	public void dispose() {
		if (jitWatchMessageConsole != null) {
			IToolBarManager toolBarManager = actionBars.getToolBarManager();

			toolBarManager.remove(REMOVE_ACTION_ID);
			removeAction = null;

			toolBarManager.remove(REMOVE_ALL_ACTION_ID);
			removeAllAction = null;
		}
	}

	@Override
	public void activated() {
	}

	@Override
	public void deactivated() {
	}

}
