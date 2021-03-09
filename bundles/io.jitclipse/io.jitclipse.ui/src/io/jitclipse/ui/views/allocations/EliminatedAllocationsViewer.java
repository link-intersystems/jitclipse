package io.jitclipse.ui.views.allocations;

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
import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.IMemberByteCode;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.JitCompiler;
import io.jitclipse.core.model.allocation.IEliminatedAllocation;

public class EliminatedAllocationsViewer extends TableViewer {

	public class EliminatedAllocationCellLabelProvider extends CellLabelProvider implements ITableLabelProvider {

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
			IEliminatedAllocation eliminatedAllocation = (IEliminatedAllocation) element;
			String text = "";

			IMethod method = eliminatedAllocation.getMethod();
			switch (columnIndex) {
			case 0:
				text = method.getType().getName();
				break;
			case 1:
				text = method.toSignatureString();
				break;
			case 2:
				ICompilation compilation = eliminatedAllocation.getCompilation();
				JitCompiler compiler = compilation.getCompiler();
				text = compiler.name();
				break;
			case 3:
				IMemberByteCode memberByteCode = method.getMemberByteCode();
				text = Integer.toString(memberByteCode.getByteCodeInstruction());
				break;
			case 4:
				text = eliminatedAllocation.getOptimization().getStrategy().name();
				break;
			case 5:
				IClass eliminatedType = eliminatedAllocation.getEliminatedType();
				if (eliminatedType != null) {
					text = eliminatedType.getName();
				}
				break;
			default:
				break;
			}
			return text;
		}

	}

	private EliminatedAllocationCellLabelProvider cellLabelProvider = new EliminatedAllocationCellLabelProvider();

	public EliminatedAllocationsViewer(Composite parent, int style) {
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
		tblclmnHow.setWidth(100);
		tblclmnHow.setText("How");

		TableViewerColumn tableViewerColumnEliminatedType = new TableViewerColumn(this, SWT.NONE);
		tableViewerColumnEliminatedType.setLabelProvider(cellLabelProvider);
		TableColumn tblclmnEliminatedType = tableViewerColumnEliminatedType.getColumn();
		tblclmnEliminatedType.setWidth(100);
		tblclmnEliminatedType.setText("Eliminated Type");
	}

}
