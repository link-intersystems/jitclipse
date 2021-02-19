package io.jitclipse.ui.navigator;

import static org.eclipse.jface.viewers.IDecoration.BOTTOM_RIGHT;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import com.link_intersystems.eclipse.ui.jface.viewers.AbstractChangeSupportLabelProvider;
import com.link_intersystems.eclipse.ui.jface.viewers.progress.AbstractProgressRenderer;
import com.link_intersystems.eclipse.ui.jface.viewers.progress.IncreasingDotsProgressRenderer;
import com.link_intersystems.eclipse.ui.jface.viewers.progress.ProgressIndicator;
import com.link_intersystems.eclipse.ui.jface.viewers.progress.ProgressIndicatorLabelProvider;

import io.jitclipse.core.resources.IHotspotLogFile;
import io.jitclipse.core.resources.IHotspotLogFolder;
import io.jitclipse.ui.JitUIImages;
import io.jitclipse.ui.JitUIPlugin;

public class JitNavigatorLabelProvider extends AbstractChangeSupportLabelProvider implements IStyledLabelProvider {

	private ProgressIndicatorLabelProvider progressIndicatorLabelProvider = new ProgressIndicatorLabelProvider(
			"Loading");
	private JitModelLabelProvider modelLabelProvider = new JitModelLabelProvider();
	private JitUIImages jitImages = JitUIPlugin.getInstance().getJitUIImages();

	public JitNavigatorLabelProvider() {
		AbstractProgressRenderer unknownProgressRenderer = new IncreasingDotsProgressRenderer();
		progressIndicatorLabelProvider.setUnknownProgressRenderer(unknownProgressRenderer);
	}

	@Override
	public String getText(Object element) {
		if (IHotspotLogFolder.class.isInstance(element)) {
			return "Hotspot Log Files";
		} else if (IHotspotLogFile.class.isInstance(element)) {
			IHotspotLogFile hotspotLogFile = IHotspotLogFile.class.cast(element);
			return hotspotLogFile.getFile().getName();
		} else if (IFile.class.isInstance(element)) {
			IFile hotspotLogFileModel = IFile.class.cast(element);
			return hotspotLogFileModel.getName();
		} else if (ProgressIndicator.class.isInstance(element)) {
			ProgressIndicator progressIndicator = ProgressIndicator.class.cast(element);
			registerChangeSource(progressIndicator);
			return progressIndicatorLabelProvider.getText(progressIndicator);
		}
		return modelLabelProvider.getText(element);
	}

	@Override
	public StyledString getStyledText(Object element) {
		if (ProgressIndicator.class.isInstance(element)) {
			ProgressIndicator progressIndicator = ProgressIndicator.class.cast(element);
			registerChangeSource(progressIndicator);
			return progressIndicatorLabelProvider.getStyledText(progressIndicator);
		}

		String text = getText(element);
		if (text != null) {
			return new StyledString(text);
		}

		return null;
	}

	@Override
	public Image getImage(Object element) {
		if (IHotspotLogFolder.class.isInstance(element)) {
			Image folderImage = jitImages.getFileFolderImage();
			ImageDescriptor hotDescriptor = jitImages.getHotDescriptor();
			DecorationOverlayIcon decoration = new DecorationOverlayIcon(folderImage, hotDescriptor, BOTTOM_RIGHT);
			return decoration.createImage();
		} else if (IAdaptable.class.isInstance(element)) {
			IAdaptable adaptable = (IAdaptable) element;
			IHotspotLogFile hotspotLogFile = adaptable.getAdapter(IHotspotLogFile.class);
			if (hotspotLogFile != null) {
				Image folderImage = jitImages.getFileImage();
				ImageDescriptor hotDescriptor = jitImages.getHotDescriptor();
				DecorationOverlayIcon decoration = new DecorationOverlayIcon(folderImage, hotDescriptor, BOTTOM_RIGHT);
				return decoration.createImage();
			}
		}
		return null;
	}

}
