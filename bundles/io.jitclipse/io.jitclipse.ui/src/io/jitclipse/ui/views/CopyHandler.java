/*******************************************************************************
 * Copyright (c) 2006, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 ******************************************************************************/
package io.jitclipse.ui.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.HandlerEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

/**
 * This handler copies a textual representation of the current selection to the
 * clipboard.
 */
class CopyHandler extends AbstractHandler implements ISelectionChangedListener {

	private Display display;
	private ISelectionProvider selectionSource;
	private ISelectionToString selectionToString;

	public CopyHandler(Display display, ISelectionProvider selectionSource, ISelectionToString selectionToString) {
		this.display = display;
		this.selectionSource = selectionSource;
		this.selectionToString = selectionToString;
		selectionSource.addSelectionChangedListener(this);
	}

	@Override
	public boolean isEnabled() {
		return !selectionSource.getSelection().isEmpty();
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = selectionSource.getSelection();
		String text = selectionToString.toString(selection);

		copy(text);

		return null;
	}

	private void copy(String text) {
		final Clipboard cb = new Clipboard(display);
		final TextTransfer transfer = TextTransfer.getInstance();
		cb.setContents(new Object[] { text }, new Transfer[] { transfer });
		cb.dispose();
	}

	@Override
	public void dispose() {
		selectionSource.removeSelectionChangedListener(this);
	}

	public void selectionChanged(SelectionChangedEvent event) {
		fireHandlerChanged(new HandlerEvent(this, true, false));
	}

}
