package io.jitclipse.jitwatch.core.launch;

import io.jitclipse.core.launch.IJitArgs;
import io.jitclipse.core.launch.IJitArgsProvider;

public class JitWatchArgProvider implements IJitArgsProvider {

	@Override
	public IJitArgs createJitArgs() {
		return new OpenJDKJitArgs();
	}

}
