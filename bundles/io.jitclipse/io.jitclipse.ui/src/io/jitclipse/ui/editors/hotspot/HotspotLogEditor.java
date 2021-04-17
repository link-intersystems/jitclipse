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

import java.io.File;
import java.net.URI;
import java.util.function.Supplier;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.link_intersystems.eclipse.core.runtime.runtime.progress.IProgress;
import com.link_intersystems.eclipse.core.runtime.runtime.progress.LazyProgressMonitorDelegate;
import com.link_intersystems.eclipse.ui.swt.widgets.Display2;

import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.allocation.IEliminatedAllocationList;
import io.jitclipse.core.model.lock.IOptimisedLockList;
import io.jitclipse.core.model.suggestion.ISuggestionList;
import io.jitclipse.core.resources.IHotspotLogFile;
import io.jitclipse.ui.editors.DeferredConsumer;
import io.jitclipse.ui.editors.xml.HotspotXMLEditor;
import io.jitclipse.ui.views.allocations.EliminatedAllocationsViewer;
import io.jitclipse.ui.views.locks.OptimizedLocksViewer;
import io.jitclipse.ui.views.suggestions.SuggestionsViewer;

public class HotspotLogEditor extends MultiPageEditorPart implements IResourceChangeListener {

	public static final String ID = "io.jitclipse.ui.hotspot.HotspotLogEditor";

	private Supplier<IWorkspace> workspaceSupplier = ResourcesPlugin::getWorkspace;
	private IWorkspace workspace;

	private TextEditor sourceEditor;

	private SuggestionsViewer suggestionsViewer;
	private DeferredConsumer<ISuggestionList> suggestionsReportModelConsumer = new DeferredConsumer<>();

	private OptimizedLocksViewer optimizedLocksViewer;
	private DeferredConsumer<IOptimisedLockList> optimizedLockReportModelConsumer = new DeferredConsumer<>();

	private EliminatedAllocationsViewer eliminatedAllocationsViewer;
	private DeferredConsumer<IEliminatedAllocationList> eliminatedAllocationsReportModelConsumer = new DeferredConsumer<>();

	private DetailedProgressWidget progressWidget;
	private LazyProgressMonitorDelegate lazyProgressMonitorDelegate = new LazyProgressMonitorDelegate();

	private IHotspotLogFile hotspotLogFile;

	public HotspotLogEditor() {
	}

	protected void createPages() {
		boolean isLoading = false;

		if (hotspotLogFile != null) {
			IProgress progress = hotspotLogFile.getProgress();
			isLoading = progress != null;
		}

		if (isLoading) {
			createProgressPage();
		} else {
			createSuggestionsPage();
			createEliminatedAllocationsPage();
			createOptimizedLocksPage();
			createSourcePage();
		}
	}

	private void createProgressPage() {
		Composite progressComposite = new Composite(getContainer(), SWT.BORDER);
		progressComposite.setLayout(new GridLayout(1, false));
		progressWidget = new DetailedProgressWidget(progressComposite);
		progressWidget.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

		IProgressMonitor eventQueueProxy = Display2.createEventQueueProxy(IProgressMonitor.class, progressWidget);
		lazyProgressMonitorDelegate.setDelegate(eventQueueProxy);

		addPage(progressComposite, "Loading");
	}

	private void createSuggestionsPage() {
		suggestionsViewer = new SuggestionsViewer(getContainer(), SWT.NONE);
		addPage(suggestionsViewer.getControl(), "Suggestions");

		suggestionsReportModelConsumer.setTargetConsumer(suggestionsViewer::setInput);
	}

	private void createOptimizedLocksPage() {
		optimizedLocksViewer = new OptimizedLocksViewer(getContainer(), SWT.NONE);
		addPage(optimizedLocksViewer.getControl(), "Optimized Locks");

		optimizedLockReportModelConsumer.setTargetConsumer(optimizedLocksViewer::setInput);
	}

	private void createEliminatedAllocationsPage() {
		eliminatedAllocationsViewer = new EliminatedAllocationsViewer(getContainer(), SWT.NONE);
		addPage(eliminatedAllocationsViewer.getControl(), "Eliminated Allocations");

		eliminatedAllocationsReportModelConsumer.setTargetConsumer(eliminatedAllocationsViewer::setInput);
	}

	private void createSourcePage() {
		try {
			sourceEditor = new HotspotXMLEditor();
			addPage(sourceEditor, getEditorInput(), "Source");
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
		}
	}

	private void addPage(Control control, String pageText) {
		int index = addPage(control);
		setPageText(index, pageText);
	}

	private void addPage(IEditorPart editor, IEditorInput input, String pageText) throws PartInitException {
		int index = addPage(editor, input);
		setPageText(index, pageText);
	}

	public void refresh() {
		removePages();
		lazyProgressMonitorDelegate.setDelegate(null);

		createPages();

		getContainer().layout(true);
	}

	private void removePages() {
		while (getPageCount() > 0) {
			removePage(0);
		}
	}

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IURIEditorInput");
		super.init(site, editorInput);

		workspace = workspaceSupplier.get();
		workspace.addResourceChangeListener(this);
	}

	public void dispose() {
		workspace.removeResourceChangeListener(this);

		super.dispose();

		workspace = null;

		sourceEditor = null;

		suggestionsReportModelConsumer.setTargetConsumer(null);
		suggestionsViewer = null;

		eliminatedAllocationsReportModelConsumer.setTargetConsumer(null);
		eliminatedAllocationsViewer = null;

		optimizedLockReportModelConsumer.setTargetConsumer(null);
		optimizedLocksViewer = null;
	}

	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}

	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);

		updateTitleAndTooltip(input);

		IFileEditorInput fileEditorInput = (IFileEditorInput) input;
		IFile file = fileEditorInput.getFile();
		IHotspotLogFile hotspotLogFile = file.getAdapter(IHotspotLogFile.class);

		setHotspotLogFile(hotspotLogFile);
	}

	private void setHotspotLogFile(IHotspotLogFile hotspotLogFile) {
		this.hotspotLogFile = hotspotLogFile;

		IHotspotLog hotspotLog = hotspotLogFile.getHotspotLog();
		if (hotspotLog == null) {
			hotspotLogFile.open((hlf) -> Display2.syncExec(() -> {
				setHotspotLog(hlf.getHotspotLog());
				refresh();
				getContainer().redraw();
			}), lazyProgressMonitorDelegate);
		} else {
			setHotspotLog(hotspotLog);
		}
	}

	private void updateTitleAndTooltip(IEditorInput input) {
		String title = getPartName();
		String toolTip = getTitleToolTip();

		if (input instanceof IURIEditorInput) {
			IURIEditorInput iuriEditorInput = (IURIEditorInput) input;
			URI uri = iuriEditorInput.getURI();
			File file = new File(uri);

			title = file.getName();
			toolTip = file.getAbsolutePath();
		} else if (input instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput) input;
			IFile file = fileEditorInput.getFile();

			title = file.getName();
			toolTip = file.getFullPath().toOSString();
		}

		setPartName(title);
		setTitleToolTip(toolTip);
	}

	private void setHotspotLog(IHotspotLog hotspotLog) {
		suggestionsReportModelConsumer.accept(hotspotLog.getSuggestionList());
		eliminatedAllocationsReportModelConsumer.accept(hotspotLog.getEliminatedAllocationList());
		optimizedLockReportModelConsumer.accept(hotspotLog.getOptimizedLockList());
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(() -> {
				IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
				for (int i = 0; i < pages.length; i++) {
					if (((FileEditorInput) sourceEditor.getEditorInput()).getFile().getProject()
							.equals(event.getResource())) {
						IEditorPart editorPart = pages[i].findEditor(sourceEditor.getEditorInput());
						pages[i].closeEditor(editorPart, true);
					}
				}
			});
		}
	}

}
