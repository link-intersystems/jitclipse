package io.jitclipse.ui.launch;

import org.eclipse.debug.ui.actions.ContextualLaunchAction;

import io.jitclipse.core.JitCorePlugin;

public class JitWatchContextualLaunchAction extends ContextualLaunchAction {

  public JitWatchContextualLaunchAction() {
    super(JitCorePlugin.LAUNCH_MODE);
  }

}
