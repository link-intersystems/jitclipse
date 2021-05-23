package io.jitclipse.triview.ui.editors.multi;

import org.eclipse.ui.IEditorPart;

/**
 * Abstract base class for managing the installation/deinstallation of global
 * actions for multi-page editors.
 * <p>
 * Subclasses must implement <code>setActivePage</code>, and may reimplement any
 * of the following methods:
 * </p>
 * <ul>
 * <li><code>contributeToMenu</code> - reimplement to contribute to menu</li>
 * <li><code>contributeToToolBar</code> - reimplement to contribute to tool
 * bar</li>
 * <li><code>contributeToStatusLine</code> - reimplement to contribute to status
 * line</li>
 * </ul>
 */
public abstract class MultiEditorEditorActionBarContributor extends EditorActionBarContributor {
	/**
	 * Creates a multi-page editor action contributor.
	 */
	protected MultiEditorEditorActionBarContributor() {
		super();
	}

	/*
	 * Registers the contributor with the multi-page editor for future editor action
	 * redirection when the active page is changed, and sets the active page.
	 */
	@Override
	public void setActiveEditor(IEditorPart part) {
		IEditorPart activeNestedEditor = null;
		if (part instanceof MultiEditorEditorPart) {
			activeNestedEditor = ((MultiEditorEditorPart) part).getActiveEditor();
		}
		setActivePage(activeNestedEditor);
	}

	/**
	 * Sets the active page of the the multi-page editor to be the given editor.
	 * Redirect actions to the given editor if actions are not already being sent to
	 * it.
	 * <p>
	 * This method is called whenever the page changes. Subclasses must implement
	 * this method to redirect actions to the given editor (if not already directed
	 * to it).
	 * </p>
	 *
	 * @param activeEditor the new active editor, or <code>null</code> if there is
	 *                     no active page, or if the active page does not have a
	 *                     corresponding editor
	 */
	public abstract void setActivePage(IEditorPart activeEditor);
}
