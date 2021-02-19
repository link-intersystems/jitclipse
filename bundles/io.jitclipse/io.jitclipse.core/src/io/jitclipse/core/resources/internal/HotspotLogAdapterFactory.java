package io.jitclipse.core.resources.internal;

import org.eclipse.core.runtime.IAdapterFactory;

import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.resources.IHotspotLogFile;

public class HotspotLogAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (IHotspotLogFile.class.isInstance(adaptableObject)) {
			IHotspotLogFile hotspotLogFile = IHotspotLogFile.class.cast(adaptableObject);
			return adapterType.cast(hotspotLogFile.getHotspotLog());
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class<?>[] { IHotspotLog.class };
	}

}
