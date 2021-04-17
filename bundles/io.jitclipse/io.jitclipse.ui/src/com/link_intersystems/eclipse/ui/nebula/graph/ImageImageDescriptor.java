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