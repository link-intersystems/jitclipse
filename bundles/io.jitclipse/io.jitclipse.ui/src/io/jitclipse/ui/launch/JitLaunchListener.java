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
package io.jitclipse.ui.launch;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.link_intersystems.eclipse.core.runtime.runtime.jobs.FutureJob;
import com.link_intersystems.eclipse.ui.jface.controller.PerspectiveOpenerController;
import com.link_intersystems.eclipse.ui.swt.widgets.Display2;

import io.jitclipse.core.launch.IJitLaunch;
import io.jitclipse.core.launch.IJitLaunchListener;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.resources.IHotspotLogFile;
import io.jitclipse.ui.JitUIPlugin;
import io.jitclipse.ui.navigator.HotspotNavigator;
import io.jitclipse.ui.perspectives.HotspotPerspective;
import io.jitclipse.ui.preferences.PreferenceConstants;

public class JitLaunchListener implements IJitLaunchListener {

	@Override
	public void finished(IJitLaunch jitLaunch) {
		Display2.syncExec(this::doFinish, jitLaunch);
	}

	private void doFinish(IJitLaunch jitLaunch) {
		IHotspotLogFile hotspotLogFile = jitLaunch.getHotspotLogFile();
		FutureJob<IHotspotLog> parseHotspotLog = hotspotLogFile.createOpenJob("Parse Hotspot Log");
		parseHotspotLog.schedule();
		expandHotspotLogFile(jitLaunch);

		PerspectiveOpenerController perspectiveOpener = new PerspectiveOpenerController(JitUIPlugin.getInstance());
		perspectiveOpener.askOpenPerspective(HotspotPerspective.ID, PreferenceConstants.ASK_PERSPECTIVE_SWITCH,
				"Open Perspective", "Do you want to open to the hotspot perspective?",
				"Always ask when opening the hotspot perspective");

	}

	private void expandHotspotLogFile(IJitLaunch jitLaunch) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		IViewPart viewPart = activePage.findView(HotspotNavigator.ID);
		if (viewPart != null) {
			HotspotNavigator hotspotNavigator = (HotspotNavigator) viewPart;
			IHotspotLogFile hotspotLogFile = jitLaunch.getHotspotLogFile();
			hotspotNavigator.expandHotspotLogFile(hotspotLogFile);
		}
	}

}
