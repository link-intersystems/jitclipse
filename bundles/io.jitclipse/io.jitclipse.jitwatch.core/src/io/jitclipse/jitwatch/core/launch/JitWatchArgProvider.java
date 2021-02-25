package io.jitclipse.jitwatch.core.launch;

import io.jitclipse.core.launch.IJitArgs;
import io.jitclipse.core.launch.IJitArgsProvider;
import io.jitclipse.core.launch.IJitExecutionEnvironment;

public class JitWatchArgProvider implements IJitArgsProvider {


	@Override
	public IJitArgs createJitArgs(IJitExecutionEnvironment jitExecutionEnvironment) {
		return new OpenJDKJitArgs();
	}

}
