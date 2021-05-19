/*******************************************************************************
 * Copyright (c) 2021 Link Intersystems GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Link Intersystems GmbH - René Link - API and implementation
 *******************************************************************************/
package io.jitclipse.jitwatch.core.parser.internal;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.adoptopenjdk.jitwatch.compilation.codecache.CodeCacheEventWalker;
import org.adoptopenjdk.jitwatch.compilation.codecache.CodeCacheWalkerResult;
import org.adoptopenjdk.jitwatch.core.IJITListener;
import org.adoptopenjdk.jitwatch.core.JITWatchConfig;
import org.adoptopenjdk.jitwatch.model.JITDataModel;
import org.adoptopenjdk.jitwatch.parser.ILogParser;
import org.adoptopenjdk.jitwatch.parser.hotspot.HotSpotLogParser;
import org.adoptopenjdk.jitwatch.report.Report;
import org.adoptopenjdk.jitwatch.report.comparator.ScoreComparator;
import org.adoptopenjdk.jitwatch.report.escapeanalysis.eliminatedallocation.EliminatedAllocationWalker;
import org.adoptopenjdk.jitwatch.report.locks.OptimisedLocksWalker;
import org.adoptopenjdk.jitwatch.report.suggestion.SuggestionWalker;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.JavaModelException;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.parser.IJitLogParser;
import io.jitclipse.core.resources.IJitProject;
import io.jitclipse.jitwatch.core.model.HotspotLog;
import io.jitclipse.jitwatch.core.parser.IParseLogParticipant;

public class JitWatchLogParser implements IJitLogParser {

	private IParseLogParticipant parseLogParticipant;

	public JitWatchLogParser() {
		this(new ParseLogParticipantBroadcaster());
	}

	public JitWatchLogParser(IParseLogParticipant parseLogParticipant) {
		this.parseLogParticipant = parseLogParticipant;
	}

	@Override
	public IHotspotLog read(IFile hotspotLogFile, IProgressMonitor monitor) throws Exception {
		HotspotLog hotspotLog = null;

		JITWatchListenerBroadcaster delegate = new JITWatchListenerBroadcaster();
		ILogParser logParser = new HotSpotLogParser(delegate);
		IJITListener target = new LogParserProgressAdapter(monitor, logParser);
		delegate.addTarget(target);

		JITWatchConfig config = logParser.getConfig();
		addSourcFolders(hotspotLogFile, config);
		addClassFolders(hotspotLogFile, config);

		logParser.setConfig(config);

		File javaFile = hotspotLogFile.getLocation().toFile();

		IJITListener listener = parseLogParticipant.aboutToParse(hotspotLogFile);
		delegate.addTarget(listener);

		try {
			logParser.processLogFile(javaFile, null);

			JITDataModel jitDataModel = logParser.getModel();

			hotspotLog = new HotspotLog(hotspotLogFile, jitDataModel, logParser.getConfig());

			List<Report> suggestionReports = buildSuggestions(monitor, jitDataModel);
			hotspotLog.setSuggestionReports(suggestionReports);

			List<Report> eliminatedAllocationReports = buildEliminatedAllocationReport(monitor, jitDataModel);
			hotspotLog.setEliminatedAllocationReports(eliminatedAllocationReports);

			List<Report> optimisedLocksReport = buildOptimisedLocksReport(monitor, jitDataModel);
			hotspotLog.setOptimizedLockReports(optimisedLocksReport);

			buildCodeCacheResult(monitor, jitDataModel);

			loadMemberByteCodes(hotspotLog, SubMonitor.convert(monitor, 10));
		} catch (IOException e) {
		}

		return hotspotLog;
	}

	private void loadMemberByteCodes(IHotspotLog hotspotLog, IProgressMonitor monitor) {
		List<IClass> classes = hotspotLog.getClasses();

		monitor.beginTask("Loading Byte Codes", classes.size());

		for (IClass clazz : classes) {
			List<IMethod> methods = clazz.getMethods();
			methods.forEach(IMethod::getMemberByteCode);
			monitor.worked(1);
		}

		monitor.done();

	}

	private void addSourcFolders(IFile hotspotLogfile, JITWatchConfig config) throws JavaModelException {
		IProject project = hotspotLogfile.getProject();
		IJitProject jitProject = project.getAdapter(IJitProject.class);
		List<String> sourceLocations = jitProject.getSourceLocations();
		config.setSourceLocations(sourceLocations);
	}

	private void addClassFolders(IFile hotspotLogfile, JITWatchConfig config) throws JavaModelException {
		IProject project = hotspotLogfile.getProject();
		IJitProject jitProject = project.getAdapter(IJitProject.class);
		List<String> binaryResourceLocations = jitProject.getBinaryResourceLocations();
		config.setClassLocations(binaryResourceLocations);
	}

	private List<Report> buildSuggestions(IProgressMonitor monitor, JITDataModel dataModel) {
		monitor.setTaskName("Creating suggestions");

		SuggestionWalker walker = new SuggestionWalker(dataModel);
		List<Report> reportListSuggestions = walker.getReports(new ScoreComparator());

		monitor.setTaskName("Suggestions created " + reportListSuggestions.size());

		return reportListSuggestions;
	}

	private List<Report> buildEliminatedAllocationReport(IProgressMonitor monitor, JITDataModel dataModel) {
		monitor.setTaskName("Finding eliminated allocations");

		EliminatedAllocationWalker walker = new EliminatedAllocationWalker(dataModel);

		List<Report> reportListEliminatedAllocations = walker.getReports(new ScoreComparator());

		monitor.setTaskName("Found " + reportListEliminatedAllocations.size() + "  eliminated allocations.");
		return reportListEliminatedAllocations;
	}

	private List<Report> buildOptimisedLocksReport(IProgressMonitor monitor, JITDataModel dataModel) {
		monitor.setTaskName("Finding optimised locks");

		OptimisedLocksWalker walker = new OptimisedLocksWalker(dataModel);

		List<Report> reportListOptimisedLocks = walker.getReports(new ScoreComparator());

		monitor.setTaskName("Found " + reportListOptimisedLocks.size() + " optimised locks.");
		return reportListOptimisedLocks;
	}

	private CodeCacheWalkerResult buildCodeCacheResult(IProgressMonitor monitor, JITDataModel dataModel) {
		CodeCacheEventWalker compilationWalker = new CodeCacheEventWalker(dataModel);

		compilationWalker.walkCompilations();

		return compilationWalker.getResult();
	}

}
