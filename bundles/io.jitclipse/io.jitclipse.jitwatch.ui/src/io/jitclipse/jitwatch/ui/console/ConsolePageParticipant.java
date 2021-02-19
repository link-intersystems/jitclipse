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

	private Action removeAction;
	private JitWatchUIImages jitWatchUIImages;

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
		IActionBars actionBars = page.getSite().getActionBars();
		removeAction = new Action("Remove", jitWatchUIImages.getDeleteConsoleImageDescriptor()) {
			@Override
			public void run() {
				if (console instanceof JitWatchMessageConsole) {
					JitWatchMessageConsole jitWatchMessageConsole = (JitWatchMessageConsole) console;
					jitWatchMessageConsole.remove();
				}
			}
		};
		configureToolBar(actionBars.getToolBarManager());
	}

	protected void configureToolBar(IToolBarManager mgr) {
		mgr.appendToGroup(IConsoleConstants.LAUNCH_GROUP, removeAction);
	}

	@Override
	public void dispose() {
		removeAction = null;
	}

	@Override
	public void activated() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivated() {
		// TODO Auto-generated method stub

	}

}
