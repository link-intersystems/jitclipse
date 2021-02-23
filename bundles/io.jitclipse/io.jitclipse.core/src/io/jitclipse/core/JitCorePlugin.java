package io.jitclipse.core;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.osgi.framework.BundleContext;

import com.link_intersystems.eclipse.core.runtime.DefaultPlugin;
import com.link_intersystems.eclipse.core.runtime.IExtensionPointProxyFactory;

import io.jitclipse.core.parser.IJitLogParser;
import io.jitclipse.core.parser.internal.IJitParserExtension;

public class JitCorePlugin extends DefaultPlugin implements JitPluginContext {

	public static final String LAUNCH_MODE = "jit";
	public static final String ID = "io.jitclipse.core";

	private static JitCorePlugin inst;

	public static JitCorePlugin getInstance() {
		return inst;
	}

	public JitCorePlugin() {
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


	@Override
	public IJitLogParser getJitLogParser() throws CoreException {
		IExtensionPointProxyFactory extensionsPointProxyFactory = getExtensionsPointProxyFactory();
		List<IJitParserExtension> parserExtensions = extensionsPointProxyFactory
				.createProxies(IJitParserExtension.class);
		if (parserExtensions.size() > 0) {
			return parserExtensions.get(0).createParser();
		}
		throw new CoreException(JitStatus.NO_JIT_LOG_PARSER_AVAILABLE.getStatus());
	}

}
