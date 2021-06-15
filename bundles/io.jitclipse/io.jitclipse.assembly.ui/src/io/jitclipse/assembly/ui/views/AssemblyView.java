package io.jitclipse.assembly.ui.views;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.FindReplaceAction;

import com.link_intersystems.eclipse.ui.jface.viewers.AdaptableSelectionList;
import com.link_intersystems.eclipse.ui.jface.viewers.SelectionList;

import io.jitclipse.assembly.ui.format.AssemblyFormat;
import io.jitclipse.assembly.ui.text.rules.AssemblyScannerConfig;
import io.jitclipse.core.model.IAssembly;
import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.IMethod;

public class AssemblyView extends ViewPart implements ISelectionListener {

	public static final String ID = "io.jitclipse.assembly.ui.views.AssemblyView";

	@Inject
	IWorkbench workbench;

	private SourceViewer viewer;

	private AssemblyScannerConfig assemblyScannerConfig;

	private FindReplaceAction findAndReplace;

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

		IWorkbenchPage page = site.getPage();
		page.addSelectionListener(this);
	}

	@Override
	public void dispose() {
		assemblyScannerConfig.dispose();

		IWorkbenchPage page = getSite().getPage();
		page.removeSelectionListener(this);

		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {

		viewer = new SourceViewer(parent, null, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setEditable(false);
		viewer.setInput(new Document(""));

		assemblyScannerConfig = new AssemblyScannerConfig();
		viewer.configure(assemblyScannerConfig);
		StyledText textWidget = viewer.getTextWidget();

		ResourceManager resManager = new LocalResourceManager(JFaceResources.getResources(), parent);
		Font font = resManager.createFont(FontDescriptor.createFrom("Courier", 10, SWT.NONE));
		textWidget.setFont(font);

		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(viewer.getControl(), "io.jitclipse.assembly.ui.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		IWorkbenchPartSite site = getSite();
		ISelectionProvider selectionProvider = site.getSelectionProvider();
		ISelection selection = selectionProvider.getSelection();

		selectionChanged(site.getPart(), selection);
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

		bars.setGlobalActionHandler(ActionFactory.FIND.getId(), findAndReplace);
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(findAndReplace);
	}

	private void fillContextMenu(IMenuManager manager) {
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
	}

	private void makeActions() {
		Map<String, Object> entries = new HashMap<>();
		entries.put("label", "Find");
		MapResourceBundle mapResourceBundle = new MapResourceBundle(entries);
		findAndReplace = new FindReplaceAction(mapResourceBundle, "", getSite().getShell(), //$NON-NLS-1$
				viewer.getFindReplaceTarget());
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
		if (method != null) {
			IAssembly assembly = method.getLatestCompilation().map(ICompilation::getAssembly).orElse(null);
			if (assembly != null) {
				AssemblyFormat assemblyFormat = new AssemblyFormat();
				CharSequence formattedAssembly = assemblyFormat.format(assembly);

				Document document = new Document(formattedAssembly.toString());
				assemblyScannerConfig.configure(document);
				viewer.setInput(document);
			} else {
				IClass type = method.getType();
				StringBuilder sb = new StringBuilder();
				sb.append("#Assembly not available for ");
				sb.append("\n#");
				sb.append(type.getName());
				sb.append('.');
				sb.append(method.toSignatureString());
				viewer.setInput(new Document(sb.toString()));
			}

		}

		findAndReplace.update();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (IFindReplaceTarget.class.isAssignableFrom(adapter)) {
			return (T) viewer.getFindReplaceTarget();
		}
		return super.getAdapter(adapter);
	}
}
