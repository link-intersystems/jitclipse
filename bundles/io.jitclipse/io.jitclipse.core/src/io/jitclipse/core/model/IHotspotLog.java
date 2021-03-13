package io.jitclipse.core.model;

import java.util.List;

import org.eclipse.core.resources.IFile;

import com.link_intersystems.eclipse.core.runtime.runtime.IAdaptable2;

import io.jitclipse.core.model.allocation.IEliminatedAllocationList;
import io.jitclipse.core.model.lock.IOptimisedLockList;
import io.jitclipse.core.model.suggestion.ISuggestionList;

public interface IHotspotLog extends IAdaptable2 {

	public IFile getLogFileLocation();

	public List<IPackage> getRootPackages();

	public boolean contains(IPackage packageObj);

	public IClassList getClasses();

	public ISuggestionList getSuggestionList();

	public IEliminatedAllocationList getEliminatedAllocationList();

	public IOptimisedLockList getOptimizedLockList();

	public ICompilationList getCompilationList();


}
