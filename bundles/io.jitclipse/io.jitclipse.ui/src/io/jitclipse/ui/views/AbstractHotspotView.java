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

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import com.link_intersystems.eclipse.core.runtime.runtime.IPluginLog;
import com.link_intersystems.eclipse.ui.jface.viewers.AdaptableSelectionList;
import com.link_intersystems.eclipse.ui.jface.viewers.SelectionList;
import com.link_intersystems.lang.reflect.Class2;

import io.jitclipse.core.jdt.JavaElementLocator;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IMemberByteCode;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IPackage;
import io.jitclipse.core.resources.IJitProject;
import io.jitclipse.ui.JitUIImages;
import io.jitclipse.ui.JitUIPlugin;
import io.jitclipse.ui.navigator.HotspotNavigator;
import io.jitclipse.ui.navigator.JitNavigatorContentProvider;
import io.jitclipse.ui.perspectives.HotspotPerspective;

public abstract class AbstractHotspotView<T> extends ViewPart implements ISelectionListener {

	private static TypeVariable<?> TYPE_VAR = Class2.get(AbstractHotspotView.class).getTypeVariable("T");

	private IPluginLog pluginLog = JitUIPlugin.getInstance().getPluginLog();
	private List<IHandler> handlers = new ArrayList<IHandler>();

	private StructuredViewer structuredViewer;
	private Action doubleClickAction;
	private Action linkWithSelection;

	private HotspotViewerFilter hotspotViewerFilter;

	private JitSelection jitSelection = new JitSelection();

	private IHotspotLog hotspotLog;

	@Override
	public final void createPartControl(Composite parent) {
		structuredViewer = createViewer(parent);

		hotspotViewerFilter = new HotspotViewerFilter(structuredViewer, this::toHotspotViewElement);
		hotspotViewerFilter.setViewSite(getSite());
		hotspotViewerFilter.setViewIdToLinkWith(HotspotNavigator.ID);
		hotspotViewerFilter.setTreeContentProvider(new JitNavigatorContentProvider());
		structuredViewer.setFilters(hotspotViewerFilter);

		getSite().setSelectionProvider(structuredViewer);
		getSite().getPage().addSelectionListener(this);

		configureHelpSystem();

		registerHandlers();
		registerActions();

		syncSelectionFromNavigator();
	}

	protected void copy(String text) {
		Display display = getSite().getShell().getDisplay();
		Clipboard cb = new Clipboard(display);
		TextTransfer transfer = TextTransfer.getInstance();
		cb.setContents(new Object[] { text }, new Transfer[] { transfer });
		cb.dispose();
	}

	protected Object toHotspotViewElement(Object viewerElement) {
		return viewerElement;
	}

	private void syncSelectionFromNavigator() {
		IViewReference[] viewReferences = getSite().getPage().getViewReferences();

		for (IViewReference viewReference : viewReferences) {
			String id = viewReference.getId();
			if (HotspotPerspective.isHotspotNavigator(id)) {
				IViewPart view = viewReference.getView(false);
				if (view != null) {
					ISelection selection = view.getSite().getSelectionProvider().getSelection();
					selectionChanged(selection);
					break;
				}
			}
		}
	}

	protected void setFilterPackages(List<IPackage> packages) {
	}

	protected final void configureHelpSystem() {
		String helpId = getHelpId();

		if (helpId != null) {
			IWorkbench workbench = getWorkbench();
			workbench.getHelpSystem().setHelp(getViewer().getControl(), helpId);
		}
	}

	protected IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}

	protected String getHelpId() {
		return null;
	}

	protected abstract StructuredViewer createViewer(Composite parent);

	protected void registerActions() {
		makeActions();
		hookDoubleClickAction();
		hookContextMenu();
		contributeToActionBars();
	}

	protected Class<T> getElementType() {
		Class2<?> thisClass = Class2.get(getClass());
		return thisClass.getBoundClass(TYPE_VAR);
	}

	protected void makeActions() {
		doubleClickAction = new Action() {
			public void run() {
				StructuredViewer viewer = getViewer();
				IStructuredSelection selection = viewer.getStructuredSelection();
				Class<T> elementType = getElementType();
				SelectionList<T> typedSelectionList = new AdaptableSelectionList<>(elementType, selection);
				typedSelectionList.getFirstElement().ifPresent(AbstractHotspotView.this::doubleClicked);
			}
		};

		linkWithSelection = new Action("Link with Hotspot Navigator", IAction.AS_CHECK_BOX) {

			@Override
			public void run() {
				hotspotViewerFilter.setEnabled(isChecked());
			}

		};

		JitUIPlugin jitUIPlugin = JitUIPlugin.getInstance();
		JitUIImages jitUIImages = jitUIPlugin.getJitUIImages();
		ImageDescriptor linkDescriptor = jitUIImages.getLinkDescriptor();
		linkWithSelection.setImageDescriptor(linkDescriptor);
	}

	protected void doubleClicked(T element) {
		IMethod method = toMethod(element);
		if (method == null) {
			return;
		}

		String typeName = method.getType().getName();

		IFile logFileLocation = hotspotLog.getLogFileLocation();
		IProject actualProject = logFileLocation.getProject();
		IJitProject jitProject = actualProject.getAdapter(IJitProject.class);

		try {
			JavaElementLocator javaElementLocator = jitProject.getJavaElementLocator();
			IType javaType = javaElementLocator.findType(typeName);
			if (javaType != null) {
				IEditorPart javaEditor = JavaUI.openInEditor(javaType);
				IMemberByteCode memberByteCode = method.getMemberByteCode();
				if (memberByteCode != null) {
					int sourceLineNr = memberByteCode.getSourceLineNr();
					gotoLine(javaEditor, sourceLineNr, javaType);
				}
			}
		} catch (JavaModelException e) {
			pluginLog.logError(e);
		} catch (PartInitException e) {
			pluginLog.logError(e);
		}
	}

	protected IMethod toMethod(T element) {
		return null;
	}

	private static void gotoLine(IEditorPart editorPart, int line, IJavaElement element) throws JavaModelException {
		if (line <= 0) {
			return;
		}
		ITextEditor editor = (ITextEditor) editorPart;
		IDocumentProvider provider = editor.getDocumentProvider();
		IDocument document = provider.getDocument(editor.getEditorInput());
		try {
			if (element instanceof org.eclipse.jdt.core.IMethod) {
				ISourceRange sourceRange = ((org.eclipse.jdt.core.IMethod) element).getSourceRange();
				int start = sourceRange.getOffset();
				int end = start + sourceRange.getLength();
				start = document.getLineOfOffset(start);
				end = document.getLineOfOffset(end);
				if (start > line || end < line) {
					return;
				}
			}
			int start = document.getLineOffset(line - 1);
			editor.selectAndReveal(start, 0);
			IWorkbenchPage page = editor.getSite().getPage();
			page.activate(editor);
		} catch (BadLocationException e) {
			// ignore
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (!HotspotPerspective.isHotspotNavigator(part)) {
			return;
		}

		selectionChanged(selection);
	}

	private void selectionChanged(ISelection selection) {
		JitSelection oldSelection = this.jitSelection;
		this.jitSelection = new JitSelection(selection);
		JitSelectionChange selectionChange = new JitSelectionChange(oldSelection, jitSelection);
		if (selectionChange.isNewSelectionDifferent()) {
			onSelectionChanged(selectionChange);
		}

	}

	protected void onSelectionChanged(JitSelectionChange jitSelectionChange) {
		StructuredViewer viewer = getViewer();
		if (viewer != null) {
			jitSelectionChange.onHotspotLogChanged((hl, v) -> updateViewerInternal(v, hl), viewer);
		}
	}

	private void updateViewerInternal(Viewer viewer, IHotspotLog hotspotLog) {
		this.hotspotLog = hotspotLog;
		updateViewer(viewer, hotspotLog);
	}

	protected abstract void updateViewer(Viewer viewer, IHotspotLog hotspotLog);

	@Override
	public void dispose() {
		handlers.forEach(IHandler::dispose);
		handlers.clear();

		getSite().getPage().removeSelectionListener(this);
		hotspotViewerFilter.setViewSite(null);

		super.dispose();
	}

	private void hookDoubleClickAction() {
		StructuredViewer viewer = getViewer();
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.removeAll();
				AbstractHotspotView.this.fillContextMenu(manager);
			}
		});
		StructuredViewer viewer = getViewer();
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	protected void fillContextMenu(IContributionManager manager) {
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();

		IMenuManager menuManager = bars.getMenuManager();
		menuManager.removeAll();
		fillLocalPullDown(menuManager);

		IToolBarManager toolBarManager = bars.getToolBarManager();
		toolBarManager.removeAll();
		fillLocalToolBar(toolBarManager);

		bars.updateActionBars();
	}

	protected void fillLocalPullDown(IContributionManager manager) {
	}

	protected void fillLocalToolBar(IContributionManager manager) {
		manager.add(linkWithSelection);
	}

	private void registerHandlers() {
		activateHandler(IWorkbenchCommandConstants.EDIT_COPY,
				new CopyHandler(getSite().getShell().getDisplay(), getViewer(), new ISelectionToString() {

					@Override
					public String toString(ISelection selection) {
						return selectionToString(selection);
					}
				}));
	}

	protected String selectionToString(ISelection selection) {
		return null;
	}

	private void activateHandler(String id, IHandler handler) {
		final IHandlerService hs = (IHandlerService) getSite().getService(IHandlerService.class);
		hs.activateHandler(id, handler);
		handlers.add(handler);
	}

	protected StructuredViewer getViewer() {
		return structuredViewer;
	}

	@Override
	public void setFocus() {
		getViewer().getControl().setFocus();
	}

}