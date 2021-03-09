package io.jitclipse.ui.views.suggestions;

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
import io.jitclipse.core.model.suggestion.ISuggestion;

public class SuggestionsViewer extends TableViewer {

	public class SuggestionCellLabelProvider extends CellLabelProvider implements ITableLabelProvider {

		@Override
		public void update(ViewerCell cell) {
			Object element = cell.getElement();
			if (ISuggestion.class.isInstance(element)) {
				ISuggestion suggestion = ISuggestion.class.cast(element);

				int columnIndex = cell.getColumnIndex();
				String text = getColumnText(suggestion, columnIndex);

				cell.setText(text);
			}
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			ISuggestion suggestion = (ISuggestion) element;
			String text = "";

			switch (columnIndex) {
			case 0:
				text = Integer.toString(suggestion.getScore());
				break;
			case 1:
				text = suggestion.getType();
				break;
			case 2:
				IMethod method = suggestion.getMethod();
				IClass type = method.getType();
				IMemberByteCode memberByteCode = method.getMemberByteCode();
				if (memberByteCode == null) {
					text = type.getName() + "\n" + method.toSignatureString();
				} else {
					text = type.getName() + "\n" + method.toSignatureString() + "\nline "
							+ memberByteCode.getSourceLineNr();
				}

				break;
			case 3:
				text = suggestion.getText();
				break;
			default:
				break;
			}
			return text;
		}

	}

	private SuggestionCellLabelProvider cellLabelProvider = new SuggestionCellLabelProvider();

	public SuggestionsViewer(Composite parent, int style) {
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
		TableViewerColumn tableViewerColumnScore = new TableViewerColumn(this, SWT.NONE);
		tableViewerColumnScore.setLabelProvider(cellLabelProvider);
		TableColumn tblclmnScore = tableViewerColumnScore.getColumn();
		tblclmnScore.setWidth(100);
		tblclmnScore.setText("Score");

		TableViewerColumn tableViewerColumnType = new TableViewerColumn(this, SWT.NONE);
		tableViewerColumnType.setLabelProvider(cellLabelProvider);
		TableColumn tblclmnType = tableViewerColumnType.getColumn();
		tblclmnType.setWidth(100);
		tblclmnType.setText("Type");

		TableViewerColumn tableViewerColumnCaller = new TableViewerColumn(this, SWT.NONE);
		tableViewerColumnCaller.setLabelProvider(cellLabelProvider);
		TableColumn tblclmnCaller = tableViewerColumnCaller.getColumn();
		tblclmnCaller.setWidth(250);
		tblclmnCaller.setText("Caller");

		TableViewerColumn tableViewerColumnSuggestion = new TableViewerColumn(this, SWT.NONE);
		tableViewerColumnSuggestion.setLabelProvider(cellLabelProvider);
		TableColumn tblclmnSuggestion = tableViewerColumnSuggestion.getColumn();
		tblclmnSuggestion.setWidth(600);
		tblclmnSuggestion.setText("Suggestion");
	}

}
