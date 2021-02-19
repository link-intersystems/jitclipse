package com.link_intersystems.eclipse.ui.nebula.graph;

import java.util.function.Supplier;

import org.eclipse.nebula.visualization.xygraph.figures.IXYGraph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

public class SaveSnapshotAction extends AbstractXYGraphAction {

	public SaveSnapshotAction(Supplier<IXYGraph> graphSupplier) {
		super(graphSupplier, "Snapshot");
	}

	@Override
	protected void run(IXYGraph xyGraph) {
		final ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { xyGraph.getImage().getImageData() };
		final FileDialog dialog = new FileDialog(Display.getDefault().getShells()[0], SWT.SAVE);
		dialog.setFilterNames(new String[] { "PNG Files", "All Files (*.*)" });
		dialog.setFilterExtensions(new String[] { "*.png", "*.*" }); // Windows
		final String path = dialog.open();
		if ((path != null) && !path.equals("")) {
			loader.save(path, SWT.IMAGE_PNG);
		}
	}

}
