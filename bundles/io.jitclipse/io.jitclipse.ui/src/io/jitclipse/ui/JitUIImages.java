package io.jitclipse.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.link_intersystems.eclipse.ui.jface.resource.ImageRef;

public interface JitUIImages {

	@ImageRef("platform:/plugin/org.eclipse.ui/icons/full/obj16/file_obj.png")
	public Image getFileImage();

	@ImageRef("platform:/plugin/org.eclipse.ui.ide/icons/full/obj16/fileFolderType_filter.png")
	public Image getFileFolderImage();

	@ImageRef("platform:/plugin/org.eclipse.jdt.ui/icons/full/obj16/package_obj.png")
	public Image getPackageImage();

	@ImageRef("platform:/plugin/org.eclipse.jdt.ui/icons/full/elcl16/class_obj.png")
	public Image getClassImage();

	@ImageRef("platform:/plugin/org.eclipse.jdt.ui/icons/full/elcl16/public_co.png")
	public Image getMethodImage();

	@ImageRef("icons/full/obj16/link.png")
	public ImageDescriptor getLinkDescriptor();

	@ImageRef("icons/full/ovr/hot.png")
	public ImageDescriptor getHotDescriptor();

}
