package io.jitclipse.ui.launch;

import org.eclipse.debug.ui.actions.AbstractLaunchToolbarAction;

import io.jitclipse.ui.JitUIPlugin;

public class JitWatchToolbarAction extends AbstractLaunchToolbarAction {

	public JitWatchToolbarAction() {
		super(JitUIPlugin.ID_JITWATCH_LAUNCH_GROUP);
	}

}
