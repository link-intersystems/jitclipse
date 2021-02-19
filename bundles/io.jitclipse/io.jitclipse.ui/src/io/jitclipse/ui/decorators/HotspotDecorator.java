package io.jitclipse.ui.decorators;

import static org.eclipse.jface.viewers.IDecoration.BOTTOM_RIGHT;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecorationContext;
import org.eclipse.swt.graphics.Image;

import com.link_intersystems.eclipse.ui.jface.viewers.AbstractStatfulLabelDecorator;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IPackage;
import io.jitclipse.ui.JitUIImages;
import io.jitclipse.ui.JitUIPlugin;

public class HotspotDecorator extends AbstractStatfulLabelDecorator {

	private JitUIImages jitUIImages = JitUIPlugin.getInstance().getJitUIImages();

	@Override
	public Image decorateImage(Image image, Object element) {
		if (element instanceof IPackage) {
			IPackage aPackage = (IPackage) element;
			if (aPackage.containsHotspots()) {
				return createHotspotDecoration(image);
			}
		} else if (element instanceof IClass) {
			IClass aClass = (IClass) element;
			if (aClass.containsHotspots()) {
				return createHotspotDecoration(image);
			}

		} else if (element instanceof IMethod) {
			IMethod aMethod = (IMethod) element;
			if (aMethod.isHot()) {
				return createHotspotDecoration(image);
			}

		}
		return null;
	}

	private Image createHotspotDecoration(Image image) {
		ImageDescriptor hotDescriptor = jitUIImages.getHotDescriptor();
		DecorationOverlayIcon decoration = new DecorationOverlayIcon(image, hotDescriptor, BOTTOM_RIGHT);
		return decoration.createImage();
	}

	@Override
	public String decorateText(String text, Object element) {
		return text;
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public Image decorateImage(Image image, Object element, IDecorationContext context) {
		return null;
	}

	@Override
	public String decorateText(String text, Object element, IDecorationContext context) {
		return null;
	}

	@Override
	public boolean prepareDecoration(Object element, String originalText, IDecorationContext context) {
		return false;
	}

}
