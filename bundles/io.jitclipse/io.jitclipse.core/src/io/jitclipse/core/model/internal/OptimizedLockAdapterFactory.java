package io.jitclipse.core.model.internal;

import org.eclipse.core.runtime.IAdapterFactory;

import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.lock.IOptimisedLock;

public class OptimizedLockAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, java.lang.Class<T> adapterType) {
		if (IOptimisedLock.class.isInstance(adaptableObject)) {
			IOptimisedLock optimisedLock = IOptimisedLock.class.cast(adaptableObject);
			return getAdapter(optimisedLock, adapterType);
		}
		return null;
	}

	public <T> T getAdapter(IOptimisedLock optimisedLock, java.lang.Class<T> adapterType) {
		if (IMethod.class.isAssignableFrom(adapterType)) {
			return adapterType.cast(optimisedLock.getMethod());
		}
		return null;
	}

	@Override
	public java.lang.Class<?>[] getAdapterList() {
		return new java.lang.Class<?>[] { IMethod.class };
	}

}
