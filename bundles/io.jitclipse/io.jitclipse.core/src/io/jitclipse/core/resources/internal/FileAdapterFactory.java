package io.jitclipse.core.resources.internal;

import java.util.function.Predicate;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdapterFactory;

import io.jitclipse.core.resources.IHotspotLogFile;

public class FileAdapterFactory implements IAdapterFactory {

	private Predicate<IFile> logFileFilter;

	public FileAdapterFactory() {
		this(IHotspotLogFile::isHotspotLogFile);
	}

	public FileAdapterFactory(Predicate<IFile> logFileFilter) {
		this.logFileFilter = logFileFilter;
	}

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (IFile.class.isInstance(adaptableObject)) {
			IFile file = IFile.class.cast(adaptableObject);
			return getAdapter(file, adapterType);
		}
		return null;
	}

	public <T> T getAdapter(IFile file, Class<T> adapterType) {
		String filename = file.getName() + "." + file.getFileExtension();
		if (IHotspotLogFile.isHotspotLogFilename(filename) && IHotspotLogFile.class.isAssignableFrom(adapterType)) {
			return adapterType.cast(getHotspotLogFileAdapter(file));
		}
		return null;
	}

	public IHotspotLogFile getHotspotLogFileAdapter(IFile file) {
		if (logFileFilter.test(file)) {
			IHotspotLogFile hotspotLogFile = HotspotLogFile.getForFile(file);
			return hotspotLogFile;
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class<?>[] { IHotspotLogFile.class };
	}

}
