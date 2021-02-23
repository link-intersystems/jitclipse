package io.jitclipse.core.model;

import java.util.List;

import com.link_intersystems.eclipse.core.runtime.IAdaptable2;

public interface IPackage extends IAdaptable2 {

	public String getName();

	default IPackage getRoot() {
		IPackage parentPackage = getParent();
		if (parentPackage == null) {
			return this;
		} else {
			return parentPackage.getRoot();
		}
	}

	public IPackage getParent();

	public List<IPackage> getPackages();

	public List<IClass> getClasses();

	public String getSimpleName();

	default public boolean containsHotspots() {
		for (IClass aClass : getClasses()) {
			IMethodList methods = aClass.getMethods();
			if (methods.containsHotspots()) {
				return true;
			}
		}

		for (IPackage aPackage : getPackages()) {
			if (aPackage.containsHotspots()) {
				return true;
			}
		}

		return false;
	}

}
