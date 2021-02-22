package io.jitclipse.jitwatch.core;

import org.osgi.framework.BundleContext;

import com.link_intersystems.eclipse.core.runtime.DefaultPlugin;

public class JitWatchCorePlugin extends DefaultPlugin {

	public static final String ID = "io.jitclipse.jitwatch.core";

	private static JitWatchCorePlugin inst;

	public static JitWatchCorePlugin getInstance() {
		return inst;
	}

	public JitWatchCorePlugin() {
		super(ID);
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		inst = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		inst = null;
	}

}
