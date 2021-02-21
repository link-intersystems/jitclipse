package io.jitclipse.ui.launch;

import org.eclipse.debug.ui.actions.LaunchShortcutsAction;

import io.jitclipse.ui.JitUIPlugin;

public class JitWatchAsAction extends LaunchShortcutsAction {

  public JitWatchAsAction() {
    super(JitUIPlugin.ID_JITWATCH_LAUNCH_GROUP);
  }

}
