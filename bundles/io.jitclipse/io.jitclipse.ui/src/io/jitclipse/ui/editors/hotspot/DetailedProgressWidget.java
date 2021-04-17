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
package io.jitclipse.ui.editors.hotspot;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class DetailedProgressWidget extends Composite implements IProgressMonitor {

	private ProgressIndicator progressIndicator;
	private Label taskLabel;
	private String name;
	private String subName;

	public DetailedProgressWidget(Composite parent) {
		this(parent, SWT.NONE);
	}

	public DetailedProgressWidget(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		taskLabel = new Label(this, SWT.NONE) {

			protected void checkSubclass() {
			};

			@Override
			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point computedSize = super.computeSize(wHint, hHint, changed);
				Point parentSize = progressIndicator.getSize();
				computedSize.x = parentSize.x;
				return computedSize;
			}
		};
		taskLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		progressIndicator = new ProgressIndicator(this, SWT.NONE);

	}

	@Override
	protected void checkSubclass() {
	}

	@Override
	public void beginTask(String name, int totalWork) {
		this.name = name;

		if (totalWork == UNKNOWN) {
			progressIndicator.beginAnimatedTask();
		} else {
			progressIndicator.beginTask(totalWork);
		}

		updateLabelText();
	}

	@Override
	public void layout() {
		super.layout();
		taskLabel.getBounds().width = getBounds().width;
	}

	@Override
	public void done() {
		progressIndicator.done();
		this.name = null;
		updateLabelText();
	}

	@Override
	public void internalWorked(double work) {
		progressIndicator.worked(work);
	}

	@Override
	public boolean isCanceled() {
		return false;
	}

	@Override
	public void setCanceled(boolean value) {
	}

	@Override
	public void setTaskName(String name) {
		this.name = name;
		updateLabelText();
	}

	private void updateLabelText() {
		StringBuilder sb = new StringBuilder();

		if (name != null) {
			sb.append(name);
		}

		if (subName != null) {
			if (sb.length() > 0) {
				sb.append(": ");
			}
			sb.append(subName);
		}

		taskLabel.setText(sb.toString());
		getParent().layout();
	}

	@Override
	public void subTask(String name) {
		subName = name;

		updateLabelText();
	}

	@Override
	public void worked(int work) {
		internalWorked(work);
	}

}
