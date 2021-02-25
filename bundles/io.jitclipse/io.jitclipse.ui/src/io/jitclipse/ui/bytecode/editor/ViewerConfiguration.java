package io.jitclipse.ui.bytecode.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class ViewerConfiguration extends SourceViewerConfiguration {
	private DoubleClickStrategy doubleClickStrategy;
	private ByteCodeTagScanner tagScanner;
	private ByteCodeScanner scanner;
	private ColorManager colorManager;

	public ViewerConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, ByteCodePartitionScanner.COMMENT,
				ByteCodePartitionScanner.INSTRUCTION };
	}

	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new DoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected ByteCodeScanner getByteCodeScanner() {
		if (scanner == null) {
			scanner = new ByteCodeScanner(colorManager);
			scanner.setDefaultReturnToken(new Token(new TextAttribute(colorManager.getColor(IColorConstants.DEFAULT))));
		}
		return scanner;
	}

	protected ByteCodeTagScanner getTagScanner() {
		if (tagScanner == null) {
			tagScanner = new ByteCodeTagScanner(colorManager);
			tagScanner
					.setDefaultReturnToken(new Token(new TextAttribute(colorManager.getColor(IColorConstants.STRING))));
		}
		return tagScanner;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getTagScanner());
		reconciler.setDamager(dr, ByteCodePartitionScanner.INSTRUCTION);
		reconciler.setRepairer(dr, ByteCodePartitionScanner.INSTRUCTION);

		dr = new DefaultDamagerRepairer(getByteCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(
				new TextAttribute(colorManager.getColor(IColorConstants.COMMENT)));
		reconciler.setDamager(ndr, ByteCodePartitionScanner.COMMENT);
		reconciler.setRepairer(ndr, ByteCodePartitionScanner.COMMENT);

		return reconciler;
	}

}