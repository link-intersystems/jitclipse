package io.jitclipse.jitwatch.core.model;

import org.eclipse.core.runtime.IAdapterFactory;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IPackage;

public class ClassAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, java.lang.Class<T> adapterType) {
		if (IClass.class.isInstance(adaptableObject)) {
			IClass clazz = IClass.class.cast(adaptableObject);
			return getAdapter(clazz, adapterType);
		}
		return null;
	}

	public <T> T getAdapter(IClass clazz, java.lang.Class<T> adapterType) {
		if (IHotspotLog.class.isAssignableFrom(adapterType)) {
			IPackage packgeObj = clazz.getPackage();
			IPackage rootPackage = packgeObj.getRoot();
			return adapterType.cast(rootPackage.getAdapter(adapterType));
		}
		return null;

	}

	@Override
	public java.lang.Class<?>[] getAdapterList() {
		return new java.lang.Class<?>[] { IHotspotLog.class };
	}

}
