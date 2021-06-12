package io.jitclipse.assembly.ui.views;

import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.link_intersystems.eclipse.ui.jface.viewers.AdaptableSelectionList;
import com.link_intersystems.eclipse.ui.jface.viewers.SelectionList;

import io.jitclipse.core.model.IAssembly;
import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.ui.editors.hotspot.HotspotLogEditor;

public class AssemblyView extends ViewPart implements ISelectionListener {

	public static final String ID = "io.jitclipse.assembly.ui.views.AssemblyView";

	@Inject
	IWorkbench workbench;

	private TextViewer viewer;
	private Action action1;
	private Action action2;

	private IEditorListener editorListener = new IEditorListener() {

		@Override
		public void editorChanged(IEditorReference editorReference) {
			setActiveEditor(editorReference);
		}
	};

	private Editors editors = new Editors();

	private IMethod method;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);

		editors.init();

		editors.addEditorListener(HotspotLogEditor.ID, editorListener);

		IWorkbenchPage page = site.getPage();
		page.addSelectionListener(this);
	}

	@Override
	public void dispose() {
		super.dispose();

		editors.removeEditorListener(HotspotLogEditor.ID, editorListener);
		editors.dispose();
	}

	private void setActiveEditor(IEditorReference editorReference) {
		System.out.println(editorReference);
	}

	@Override
	public void createPartControl(Composite parent) {

		viewer = new TextViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setEditable(false);
		viewer.setInput(new Document(""));

		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(viewer.getControl(), "io.jitclipse.assembly.ui.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				AssemblyView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(workbench.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Assembly", message);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		SelectionList<IMethod> methodSelection = new AdaptableSelectionList<>(IMethod.class, selection);
		Optional<IMethod> selectedMethod = methodSelection.getFirstElement();
		setMethod(selectedMethod.orElse(null));

	}

	private void setMethod(IMethod method) {
		this.method = method;

		if (method != null) {
			IAssembly assembly = method.getLatestCompilation().map(ICompilation::getAssembly).orElse(null);
			if (assembly != null) {
			}

		}
	}

}
