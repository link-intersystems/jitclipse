package io.jitclipse.core.model;

import java.util.List;

import com.link_intersystems.eclipse.core.runtime.IAdaptable2;

public interface IClass extends IAdaptable2 {

	public String getName();

	public IPackage getPackage();

	public List<IMethod> getMethods();

	public String getSimpleName();

	public IClassByteCode getByteCode();

	default public boolean containsHotspots() {
		for (IMethod method : getMethods()) {
			if (method.isHot()) {
				return true;
			}
		}
		return false;
	}
}
