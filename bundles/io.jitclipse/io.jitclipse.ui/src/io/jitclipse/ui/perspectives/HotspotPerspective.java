package io.jitclipse.ui.perspectives;

import static org.eclipse.ui.IPageLayout.BOTTOM;
import static org.eclipse.ui.IPageLayout.LEFT;
import static org.eclipse.ui.IPageLayout.RIGHT;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.console.IConsoleConstants;

import io.jitclipse.ui.views.TimelineView;
import io.jitclipse.ui.views.allocations.EliminatedAllocationsView;
import io.jitclipse.ui.views.locks.OptimizedLocksView;
import io.jitclipse.ui.views.suggestions.SuggestionsView;

public class HotspotPerspective implements IPerspectiveFactory {

	public static final String ID = "io.jitclipse.ui.perspective";
	private static final String ID_HOTSPOT_NAVIGATOR = "io.jitclipse.ui.hotspotNavigator";

	public static boolean isHotspotNavigator(IWorkbenchPart part) {
		return isHotspotNavigator(part.getSite().getId());
	}

	public static boolean isHotspotNavigator(String id) {
		return ID_HOTSPOT_NAVIGATOR.equals(id);
	}

	private IPageLayout factory;

	public HotspotPerspective() {
		super();
	}

	public void createInitialLayout(IPageLayout factory) {
		this.factory = factory;
		addViews();
		addActionSets();
		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	private void addViews() {
		IFolderLayout topLeft = factory.createFolder("topLeft", // NON-NLS-1
				LEFT, 0.25f, factory.getEditorArea());
		topLeft.addView(ID_HOTSPOT_NAVIGATOR);

		IFolderLayout bottomLeft = factory.createFolder("bottomLeft", // NON-NLS-1
				BOTTOM, 0.65f, "topLeft"); // NON-NLS-1
		bottomLeft.addView(TimelineView.ID); // NON-NLS-1
		bottomLeft.addView(IPageLayout.ID_PROGRESS_VIEW); // NON-NLS-1

		IFolderLayout bottom = factory.createFolder("bottom", // NON-NLS-1
				BOTTOM, 0.55f, factory.getEditorArea());

		bottom.addView(SuggestionsView.ID);
		bottom.addView(EliminatedAllocationsView.ID);
		bottom.addView(OptimizedLocksView.ID);

		IFolderLayout other = factory.createFolder("other", RIGHT, 0.65f, "bottom"); // NON-NLS-1
		other.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
	}

	private void addActionSets() {
		factory.addActionSet("org.eclipse.debug.ui.launchActionSet"); // NON-NLS-1
		factory.addActionSet("org.eclipse.debug.ui.debugActionSet"); // NON-NLS-1
		factory.addActionSet("org.eclipse.debug.ui.profileActionSet"); // NON-NLS-1
		factory.addActionSet("org.eclipse.jdt.debug.ui.JDTDebugActionSet"); // NON-NLS-1
		factory.addActionSet("org.eclipse.jdt.junit.JUnitActionSet"); // NON-NLS-1
		factory.addActionSet("org.eclipse.team.ui.actionSet"); // NON-NLS-1
		factory.addActionSet("org.eclipse.team.cvs.ui.CVSActionSet"); // NON-NLS-1
		factory.addActionSet("org.eclipse.ant.ui.actionSet.presentation"); // NON-NLS-1
		factory.addActionSet(JavaUI.ID_ACTION_SET);
		factory.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
		factory.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET); // NON-NLS-1
	}

	private void addPerspectiveShortcuts() {
		factory.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective"); // NON-NLS-1
		factory.addPerspectiveShortcut("org.eclipse.team.cvs.ui.cvsPerspective"); // NON-NLS-1
		factory.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective"); // NON-NLS-1
	}

	private void addNewWizardShortcuts() {
		factory.addNewWizardShortcut("org.eclipse.team.cvs.ui.newProjectCheckout");// NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");// NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");// NON-NLS-1
	}

	private void addViewShortcuts() {
		factory.addShowViewShortcut("org.eclipse.ant.ui.views.AntView"); // NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.team.ccvs.ui.AnnotateView"); // NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.pde.ui.DependenciesView"); // NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.jdt.junit.ResultView"); // NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.team.ui.GenericHistoryView"); // NON-NLS-1
		factory.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		factory.addShowViewShortcut(JavaUI.ID_PACKAGES);
		factory.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER);
		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		factory.addShowViewShortcut(IPageLayout.ID_OUTLINE);
	}

}
