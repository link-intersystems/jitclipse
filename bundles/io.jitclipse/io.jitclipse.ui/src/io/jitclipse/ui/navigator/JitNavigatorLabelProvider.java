package io.jitclipse.ui.navigator;

import static org.eclipse.jface.viewers.IDecoration.BOTTOM_RIGHT;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import com.link_intersystems.eclipse.core.runtime.progress.IProgress;
import com.link_intersystems.eclipse.ui.jface.viewers.AbstractChangeSupportLabelProvider;
import com.link_intersystems.eclipse.ui.jface.viewers.progress.AbstractProgressRenderer;
import com.link_intersystems.eclipse.ui.jface.viewers.progress.IncreasingDotsProgressRenderer;
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
		} else if (IFile.class.isInstance(element)) {
			IFile hotspotLogFileModel = IFile.class.cast(element);
			return hotspotLogFileModel.getName();
		} else if (IProgress.class.isInstance(element)) {
			IProgress progressIndicator = IProgress.class.cast(element);
			registerChangeSource(progressIndicator);
			return progressIndicatorLabelProvider.getText(progressIndicator);
		}
		return modelLabelProvider.getText(element);
	}

	@Override
	public StyledString getStyledText(Object element) {
		if (IProgress.class.isInstance(element)) {
			IProgress progress = IProgress.class.cast(element);
			registerChangeSource(progress);
			return progressIndicatorLabelProvider.getStyledText(progress);
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
				Image image = jitImages.getFileImage();
				if (hotspotLogFile.isOpened()) {
					ImageDescriptor hotDescriptor = jitImages.getHotDescriptor();
					DecorationOverlayIcon decoration = new DecorationOverlayIcon(image, hotDescriptor, BOTTOM_RIGHT);
					image = decoration.createImage();
				}
				return image;
			}
		}
		return null;
	}

}
