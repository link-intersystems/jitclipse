package io.jitclipse.ui.editors.hotspot;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
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

import com.link_intersystems.eclipse.core.runtime.progress.IProgress;
import com.link_intersystems.eclipse.core.runtime.progress.LazyProgressMonitorDelegate;
import com.link_intersystems.eclipse.ui.swt.widgets.Display2;
import com.link_intersystems.eclipse.ui.swt.widgets.SWTThreadProxyFactory;

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

	private TextEditor editor;

	private Font font;

	private StyledText text;

	private SuggestionsViewer suggestionsViewer;
	private DeferredConsumer<ISuggestionList> suggestionsReportModelConsumer = new DeferredConsumer<>();

	private OptimizedLocksViewer optimizedLocksViewer;
	private DeferredConsumer<IOptimisedLockList> optimizedLockReportModelConsumer = new DeferredConsumer<>();

	private EliminatedAllocationsViewer eliminatedAllocationsViewer;
	private DeferredConsumer<IEliminatedAllocationList> eliminatedAllocationsReportModelConsumer = new DeferredConsumer<>();

	private LazyProgressMonitorDelegate lazyProgressMonitorDelegate = new LazyProgressMonitorDelegate();

	private DetailedProgressWidget progressWidget;

	private IHotspotLogFile hotspotLogFile;

	public HotspotLogEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	void createSourcePage() {
		try {
			editor = new HotspotXMLEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, "Source");
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
		}
	}

	void createSuggestionsPage() {
		suggestionsViewer = new SuggestionsViewer(getContainer(), SWT.NONE);
		int index = addPage(suggestionsViewer.getControl());
		setPageText(index, "Suggestions");

		suggestionsReportModelConsumer.setTargetConsumer(suggestionsViewer::setInput);
	}

	void createOptimizedLocksPage() {
		optimizedLocksViewer = new OptimizedLocksViewer(getContainer(), SWT.NONE);
		int index = addPage(optimizedLocksViewer.getControl());
		setPageText(index, "Optimized Locks");

		optimizedLockReportModelConsumer.setTargetConsumer(optimizedLocksViewer::setInput);
	}

	void createEliminatedAllocationsPage() {
		eliminatedAllocationsViewer = new EliminatedAllocationsViewer(getContainer(), SWT.NONE);
		int index = addPage(eliminatedAllocationsViewer.getControl());
		setPageText(index, "Eliminated Allocations");

		eliminatedAllocationsReportModelConsumer.setTargetConsumer(eliminatedAllocationsViewer::setInput);
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

	void createProgressPage() {
		Composite progressComposite = new Composite(getContainer(), SWT.BORDER);
		progressComposite.setLayout(new GridLayout(1, false));
		progressWidget = new DetailedProgressWidget(progressComposite);
		progressWidget.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

		SWTThreadProxyFactory<IProgressMonitor> swtThreadProxyFactory = new SWTThreadProxyFactory<>(
				IProgressMonitor.class, progressWidget);
		lazyProgressMonitorDelegate.setDelegate(swtThreadProxyFactory.createProxy());

		int index = addPage(progressComposite);
		setPageText(index, "Loading");
	}

	public void refresh() {
		while (getPageCount() > 0) {
			removePage(0);
		}

		lazyProgressMonitorDelegate.setDelegate(null);

		createPages();
		getContainer().layout(true);
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
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

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IURIEditorInput");
		super.init(site, editorInput);
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
					if (((FileEditorInput) editor.getEditorInput()).getFile().getProject()
							.equals(event.getResource())) {
						IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
						pages[i].closeEditor(editorPart, true);
					}
				}
			});
		}
	}

	void setFont() {
		FontDialog fontDialog = new FontDialog(getSite().getShell());
		fontDialog.setFontList(text.getFont().getFontData());
		FontData fontData = fontDialog.open();
		if (fontData != null) {
			if (font != null)
				font.dispose();
			font = new Font(text.getDisplay(), fontData);
			text.setFont(font);
		}
	}

}
