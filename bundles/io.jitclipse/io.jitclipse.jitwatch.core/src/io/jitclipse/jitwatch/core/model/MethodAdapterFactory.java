package io.jitclipse.jitwatch.core.model;

import org.eclipse.core.runtime.IAdapterFactory;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IPackage;

public class MethodAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, java.lang.Class<T> adapterType) {
		if (IMethod.class.isInstance(adaptableObject)) {
			IMethod method = IMethod.class.cast(adaptableObject);
			return getAdapter(method, adapterType);
		}
		return null;
	}

	public <T> T getAdapter(IMethod method, java.lang.Class<T> adapterType) {
		if (IHotspotLog.class.isAssignableFrom(adapterType)) {
			IClass type = method.getType();
			IPackage packgeObj = type.getPackage();
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
