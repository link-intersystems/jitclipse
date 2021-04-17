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
package io.jitclipse.ui.views.locks;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.link_intersystems.eclipse.ui.jface.viewers.MultilineCellLabelProvider;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IMemberByteCode;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.allocation.IEliminatedAllocation;
import io.jitclipse.core.model.lock.IOptimisedLock;

public class OptimizedLocksViewer extends TableViewer {

	public class ViewerCellLabelProvider extends CellLabelProvider implements ITableLabelProvider {

		@Override
		public void update(ViewerCell cell) {
			Object element = cell.getElement();
			if (IEliminatedAllocation.class.isInstance(element)) {
				IEliminatedAllocation eliminatedAllocation = IEliminatedAllocation.class.cast(element);

				int columnIndex = cell.getColumnIndex();
				String text = getColumnText(eliminatedAllocation, columnIndex);

				cell.setText(text);
			}
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			IOptimisedLock optimisedLock = (IOptimisedLock) element;
			String text = "";

			IMethod method = optimisedLock.getMethod();
			switch (columnIndex) {
			case 0:
				IClass type = method.getType();
				text = type.getName();
				break;
			case 1:
				text = method.toSignatureString();
				break;
			case 2:
				text = optimisedLock.getCompilation().getCompiler().name();
				break;
			case 3:
				text = Integer.toString(optimisedLock.getBCI());
				break;
			case 4:
				text = optimisedLock.getHow();
				break;
			case 5:
				text = optimisedLock.getKind();
				break;
			default:
				break;
			}
			return text;
		}

	}

	private ViewerCellLabelProvider cellLabelProvider = new ViewerCellLabelProvider();

	public OptimizedLocksViewer(Composite parent, int style) {
		super(parent, SWT.BORDER | SWT.FULL_SELECTION);

		createColumns();
		configureTable();

		setContentProvider(new ArrayContentProvider());
		setLabelProvider(new MultilineCellLabelProvider(cellLabelProvider));

		GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH);
		getControl().setLayoutData(data);
	}

	private void configureTable() {
		Table table = getTable();
		TableLayout layout = new TableLayout();
		table.setLayout(layout);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	private void createColumns() {
		TableViewerColumn tableViewerColumnClass = new TableViewerColumn(this, SWT.NONE);
		tableViewerColumnClass.setLabelProvider(cellLabelProvider);
		TableColumn tblclmnClass = tableViewerColumnClass.getColumn();
		tblclmnClass.setWidth(200);
		tblclmnClass.setText("Class");

		TableViewerColumn tableViewerColumnMethod = new TableViewerColumn(this, SWT.NONE);
		tableViewerColumnMethod.setLabelProvider(cellLabelProvider);
		TableColumn tblclmnMethod = tableViewerColumnMethod.getColumn();
		tblclmnMethod.setWidth(200);
		tblclmnMethod.setText("Method");

		TableViewerColumn tableViewerColumnCompilation = new TableViewerColumn(this, SWT.NONE);
		tableViewerColumnCompilation.setLabelProvider(cellLabelProvider);
		TableColumn tblclmnCompilation = tableViewerColumnCompilation.getColumn();
		tblclmnCompilation.setWidth(100);
		tblclmnCompilation.setText("Compilation");

		TableViewerColumn tableViewerColumnBci = new TableViewerColumn(this, SWT.NONE);
		tableViewerColumnBci.setLabelProvider(cellLabelProvider);
		TableColumn tblclmnBci = tableViewerColumnBci.getColumn();
		tblclmnBci.setWidth(100);
		tblclmnBci.setText("BCI");

		TableViewerColumn tableViewerColumnHow = new TableViewerColumn(this, SWT.NONE);
		tableViewerColumnHow.setLabelProvider(cellLabelProvider);
		TableColumn tblclmnHow = tableViewerColumnHow.getColumn();
		tblclmnHow.setWidth(80);
		tblclmnHow.setText("How");

		TableViewerColumn tableViewerColumnKind = new TableViewerColumn(this, SWT.NONE);
		tableViewerColumnKind.setLabelProvider(cellLabelProvider);
		TableColumn tblclmnKind = tableViewerColumnKind.getColumn();
		tblclmnKind.setWidth(80);
		tblclmnKind.setText("Optimization Kind");
	}

}
