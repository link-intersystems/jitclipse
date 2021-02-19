package io.jitclipse.jitwatch.core.model;

import io.jitclipse.core.model.IOptimization;
import io.jitclipse.core.model.JitOptimizationStrategy;

public class Optimization implements IOptimization {

	private JitOptimizationStrategy jitOptimizationStrategy;

	public Optimization(JitOptimizationStrategy jitOptimizationStrategy) {
		this.jitOptimizationStrategy = jitOptimizationStrategy;
	}

	@Override
	public JitOptimizationStrategy getStrategy() {
		return jitOptimizationStrategy;
	}

}
