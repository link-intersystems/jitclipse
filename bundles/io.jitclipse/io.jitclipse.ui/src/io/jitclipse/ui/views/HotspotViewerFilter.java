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
package io.jitclipse.ui.views;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.link_intersystems.eclipse.ui.jface.viewers.AdaptableSelectionList;

public class HotspotViewerFilter extends LinkWithSelectionViewerFiler {

	private List<Object> selection = Collections.emptyList();
	private ITreeContentProvider treeContentProvider;
	private Function<Object, Object> toLinkedObjectConverter;

	public HotspotViewerFilter(Viewer viewer, Function<Object, Object> toLinkedObjectConverter) {
		super(viewer);
		this.toLinkedObjectConverter = toLinkedObjectConverter;
	}

	@Override
	protected void onSelectionChanged(ISelection oldSelection, ISelection newSelection) {
		if (newSelection.isEmpty()) {
			selection = Collections.emptyList();
			return;
		}

		selection = new AdaptableSelectionList<>(Object.class, newSelection);
	}

	public void setTreeContentProvider(ITreeContentProvider treeContentProvider) {
		this.treeContentProvider = treeContentProvider;
	}

	@Override
	protected boolean select(Viewer viewer, Object parentElement, Object element, ISelection selection) {
		if (selection.isEmpty()) {
			return true;
		}

		Object currentElement = toLinkedObjectConverter.apply(element);
		while (currentElement != null) {
			if (this.selection.contains(currentElement)) {
				return true;
			}

			if (treeContentProvider != null) {
				currentElement = treeContentProvider.getParent(currentElement);
			} else {
				currentElement = null;
			}
		}

		return false;

	}

}
