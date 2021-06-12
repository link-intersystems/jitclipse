package io.jitclipse.core.model.internal;

import org.eclipse.core.runtime.IAdapterFactory;

import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.allocation.IEliminatedAllocation;

public class EliminatedAllocationFactory implements IAdapterFactory {


	@Override
	public <T> T getAdapter(Object adaptableObject, java.lang.Class<T> adapterType) {
		if (IEliminatedAllocation.class.isInstance(adaptableObject)) {
			IEliminatedAllocation eliminatedAllocation = IEliminatedAllocation.class.cast(adaptableObject);
			return getAdapter(eliminatedAllocation, adapterType);
		}
		return null;
	}

	public <T> T getAdapter(IEliminatedAllocation eliminatedAllocation, java.lang.Class<T> adapterType) {
		if (IMethod.class.isAssignableFrom(adapterType)) {
			return adapterType.cast(eliminatedAllocation.getMethod());
		}
		return null;

	}

	@Override
	public java.lang.Class<?>[] getAdapterList() {
		return new java.lang.Class<?>[] { IMethod.class };
	}

}
