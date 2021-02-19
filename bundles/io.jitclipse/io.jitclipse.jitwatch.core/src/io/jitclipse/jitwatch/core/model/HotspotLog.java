package io.jitclipse.jitwatch.core.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.adoptopenjdk.jitwatch.core.JITWatchConfig;
import org.adoptopenjdk.jitwatch.model.JITDataModel;
import org.adoptopenjdk.jitwatch.model.MetaPackage;
import org.adoptopenjdk.jitwatch.model.PackageManager;
import org.adoptopenjdk.jitwatch.report.Report;
import org.eclipse.core.resources.IFile;

import io.jitclipse.jitwatch.core.model.report.ReportEliminatedAllocation;
import io.jitclipse.jitwatch.core.model.report.ReportOptimizedLock;
import io.jitclipse.jitwatch.core.model.report.ReportSuggestion;
import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.ICompilationList;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IPackage;
import io.jitclipse.core.model.allocation.EliminatedAllocationList;
import io.jitclipse.core.model.allocation.IEliminatedAllocation;
import io.jitclipse.core.model.allocation.IEliminatedAllocationList;
import io.jitclipse.core.model.lock.IOptimisedLock;
import io.jitclipse.core.model.lock.IOptimisedLockList;
import io.jitclipse.core.model.lock.OptimizedLockList;
import io.jitclipse.core.model.suggestion.ISuggestion;
import io.jitclipse.core.model.suggestion.ISuggestionList;
import io.jitclipse.core.model.suggestion.SuggestionList;

public class HotspotLog implements IHotspotLog {

	private static final int CACHE_SIZE = 128; // TODO make cache configurable through preferences

	private Map<IPackage, Boolean> containedPackageCache = new LinkedHashMap<>() {

		private static final long serialVersionUID = 3141450140715751626L;

		protected boolean removeEldestEntry(Map.Entry<IPackage, Boolean> eldest) {
			return size() > CACHE_SIZE;
		};

	};

	private ModelContext modelContext;

	private JITDataModel jitDataModel;

	private IFile hostspotLogFile;

	private List<Report> suggestionReports;
	private SuggestionList suggestionList;

	private IEliminatedAllocationList eliminatedAllocations;
	private List<Report> eliminatedAllocationReports;

	private IOptimisedLockList optimizedLockList;
	private List<Report> optimizedLockReports;

	private ICompilationList compilations;

	public HotspotLog(IFile hostspotLogFile, JITDataModel jitDataModel, JITWatchConfig config) {
		this.hostspotLogFile = hostspotLogFile;
		this.jitDataModel = jitDataModel;
		modelContext = new ModelContext(jitDataModel);
	}

	@Override
	public List<IPackage> getRootPackages() {
		PackageManager packageManager = jitDataModel.getPackageManager();
		List<MetaPackage> rootMetaPackages = packageManager.getRootPackages();
		List<IPackage> rootPackages = modelContext.getPackages(rootMetaPackages);
		return rootPackages;
	}

	@Override
	public IFile getLogFileLocation() {
		return hostspotLogFile;
	}

	@Override
	public boolean contains(IPackage packageObj) {
		if (containedPackageCache.containsKey(packageObj)) {
			return containedPackageCache.get(packageObj).booleanValue();
		}

		boolean containsPackage = contains(getRootPackages(), packageObj);
		containedPackageCache.put(packageObj, containsPackage);
		return containsPackage;
	}

	private boolean contains(List<IPackage> packages, IPackage toFind) {
		for (IPackage packageObj : packages) {
			if (packageObj.equals(toFind)) {
				return true;
			}
			List<IPackage> subPackages = packageObj.getPackages();
			if (contains(subPackages, toFind)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ISuggestionList getSuggestionList() {
		if (suggestionList == null) {
			suggestionList = getSuggestionList(jitDataModel, suggestionReports);
		}
		return suggestionList;
	}

	private SuggestionList getSuggestionList(JITDataModel jitDataModel, List<Report> suggestionReports) {
		List<ISuggestion> suggestions = new ArrayList<>();
		for (Report suggestionReport : suggestionReports) {
			suggestions.add(new ReportSuggestion(suggestionReport, modelContext));
		}
		SuggestionList suggestionList = new SuggestionList(suggestions);
		return suggestionList;
	}

	@Override
	public IEliminatedAllocationList getEliminatedAllocationList() {
		if (eliminatedAllocations == null) {
			eliminatedAllocations = getEliminatedAllocationList(jitDataModel, eliminatedAllocationReports);
		}
		return eliminatedAllocations;
	}

	private EliminatedAllocationList getEliminatedAllocationList(JITDataModel jitDataModel,
			List<Report> eliminatedAllocationReports) {
		List<IEliminatedAllocation> eliminatedAllocations = new ArrayList<>();
		for (Report eliminatedAllocationReport : eliminatedAllocationReports) {
			eliminatedAllocations.add(new ReportEliminatedAllocation(eliminatedAllocationReport, modelContext));
		}
		EliminatedAllocationList eliminatedAllocationList = new EliminatedAllocationList(eliminatedAllocations);
		return eliminatedAllocationList;
	}

	@Override
	public List<IClass> getClasses() {
		List<IPackage> rootPackages = getRootPackages();

		List<IClass> classes = getClasses(rootPackages);

		return classes;
	}

	private List<IClass> getClasses(List<IPackage> packageObjs) {
		List<IClass> classes = new ArrayList<>();
		for (IPackage packageObj : packageObjs) {
			classes.addAll(packageObj.getClasses());

			classes.addAll(getClasses(packageObj.getPackages()));
		}
		return classes;
	}

	public void setEliminatedAllocationReports(List<Report> eliminatedAllocationReports) {
		this.eliminatedAllocationReports = eliminatedAllocationReports;
	}

	public void setSuggestionReports(List<Report> suggestionReports) {
		this.suggestionReports = suggestionReports;
	}

	@Override
	public IOptimisedLockList getOptimizedLockList() {
		if (optimizedLockList == null) {
			optimizedLockList = getOptimizedLockList(jitDataModel, optimizedLockReports);
		}
		return optimizedLockList;
	}

	private OptimizedLockList getOptimizedLockList(JITDataModel jitDataModel, List<Report> optimizedLockReports) {
		List<IOptimisedLock> optimisedLocks = new ArrayList<>();
		for (Report eliminatedAllocationReport : optimizedLockReports) {
			optimisedLocks.add(new ReportOptimizedLock(eliminatedAllocationReport, modelContext));
		}
		OptimizedLockList eliminatedAllocationList = new OptimizedLockList(optimisedLocks);
		return eliminatedAllocationList;
	}

	public void setOptimizedLockReports(List<Report> optimizedLockReports) {
		this.optimizedLockReports = optimizedLockReports;
	}

	@Override
	public ICompilationList getCompilationList() {
		if (compilations == null) {
			List<ICompilation> allCompilations = new ArrayList<>();

			List<IClass> classes = getClasses();
			for (IClass clazz : classes) {
				List<IMethod> methods = clazz.getMethods();
				for (IMethod method : methods) {
					List<ICompilation> methodCompilations = method.getCompilations();
					allCompilations.addAll(methodCompilations);
				}
			}

			compilations = new CompilationList(allCompilations);
		}

		return compilations;
	}

}
