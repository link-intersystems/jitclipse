package com.link_intersystems.eclipse.core.resources;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;

public class JavaFileAdapterFactory implements IAdapterFactory {

	public static IFile toIFile(File file) {
		IFileStore fileStore;
		try {
			fileStore = EFS.getStore(file.toURI());
			return getWorkspaceFile(fileStore);
		} catch (CoreException e) {
		}

		return null;
	}

	/**
	 * Determine whether or not the <code>IFileStore</code> represents a file
	 * currently in the workspace.
	 *
	 * @param fileStore The <code>IFileStore</code> to test
	 * @return The workspace's <code>IFile</code> if it exists or <code>null</code>
	 *         if not
	 */
	private static IFile getWorkspaceFile(IFileStore fileStore) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IFile[] files = root.findFilesForLocationURI(fileStore.toURI());
		files = filterNonExistentFiles(files);
		if (files == null || files.length == 0)
			return null;

		// for now only return the first file
		return files[0];
	}

	/**
	 * Filter the incoming array of <code>IFile</code> elements by removing any that
	 * do not currently exist in the workspace.
	 *
	 * @param files The array of <code>IFile</code> elements
	 * @return The filtered array
	 */
	private static IFile[] filterNonExistentFiles(IFile[] files) {
		if (files == null)
			return null;

		int length = files.length;
		ArrayList<IFile> existentFiles = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
			if (files[i].exists())
				existentFiles.add(files[i]);
		}
		return existentFiles.toArray(new IFile[existentFiles.size()]);
	}

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (adaptableObject instanceof File) {
			return getAdapter((File) adaptableObject, adapterType);
		}
		return null;
	}

	public <T> T getAdapter(File file, Class<T> adapterType) {
		if (IFile.class.isAssignableFrom(adapterType)) {
			return adapterType.cast(toIFile(file));
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class<?>[] { File.class };
	}

}
