package com.link_intersystems.eclipse.ui.nebula.graph;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

public class ImageImageDescriptor extends ImageDescriptor {

	private Image fImage;

	public ImageImageDescriptor(Image image) {
		super();
		fImage = image;
	}

	@Override
	public ImageData getImageData(int zoom) {
		return fImage.getImageData(zoom);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj != null) && getClass().equals(obj.getClass()) && fImage.equals(((ImageImageDescriptor) obj).fImage);
	}

	@Override
	public int hashCode() {
		return fImage.hashCode();
	}

}