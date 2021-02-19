package io.jitclipse.jitwatch.core.model.report;

import java.util.HashMap;
import java.util.Map;

import org.adoptopenjdk.jitwatch.report.ReportType;

import io.jitclipse.core.model.JitOptimizationStrategy;

public class ReportTypeMapping {

	public static final ReportTypeMapping INSTANCE = new ReportTypeMapping();

	private Map<ReportType, JitOptimizationStrategy> optimizationStrategy = new HashMap<>();

	public ReportTypeMapping() {
		optimizationStrategy.put(ReportType.ELIMINATED_ALLOCATION_INLINE, JitOptimizationStrategy.INLINE);
		optimizationStrategy.put(ReportType.ELIMINATED_ALLOCATION_DIRECT, JitOptimizationStrategy.DIRECT);

		optimizationStrategy.put(ReportType.INLINE_FAILURE, JitOptimizationStrategy.INLINE);
		optimizationStrategy.put(ReportType.INLINE_SUCCESS, JitOptimizationStrategy.INLINE);

		optimizationStrategy.put(ReportType.BRANCH, JitOptimizationStrategy.BRANCH);
		optimizationStrategy.put(ReportType.CODE_CACHE, JitOptimizationStrategy.CODE_CACHE);
		optimizationStrategy.put(ReportType.HOT_THROW, JitOptimizationStrategy.HOT_THROW);
	}

	public JitOptimizationStrategy getStrategy(ReportType type) {
		return optimizationStrategy.get(type);
	}
}
