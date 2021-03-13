package io.jitclipse.core.model;

import com.link_intersystems.eclipse.core.runtime.runtime.IAdaptable2;

public interface IClass extends IAdaptable2 {

	public String getName();

	public IPackage getPackage();

	public IMethodList getMethods();

	public String getSimpleName();

	public IClassByteCode getByteCode();

	default public boolean containsHotspots() {
		return getMethods().containsHotspots();
	}

}
