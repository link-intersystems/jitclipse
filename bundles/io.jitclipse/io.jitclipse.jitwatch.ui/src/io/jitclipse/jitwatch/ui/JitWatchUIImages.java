package io.jitclipse.jitwatch.ui;

import org.eclipse.jface.resource.ImageDescriptor;

import com.link_intersystems.eclipse.ui.jface.resource.ImageRef;

public interface JitWatchUIImages {


	@ImageRef("icons/full/obj16/console.png")
	public ImageDescriptor getConsoleImageDescriptor();

	@ImageRef("platform:/plugin/org.eclipse.ui/icons/full/elcl16/remove.png")
	public ImageDescriptor getDeleteConsoleImageDescriptor();
}
