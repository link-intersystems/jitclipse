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
package io.jitclipse.core;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.runtime.CoreException;
import org.osgi.framework.BundleContext;

import com.link_intersystems.eclipse.core.runtime.runtime.DefaultPlugin;
import com.link_intersystems.eclipse.core.runtime.runtime.IExtensionPointProxyFactory;

import io.jitclipse.core.launch.Env;
import io.jitclipse.core.launch.HsdisProvider;
import io.jitclipse.core.launch.IJitArgsProvider;
import io.jitclipse.core.launch.hsdis.HsdisProviderExtension;
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

	public IJitArgsProvider getJitArgsProvider() throws CoreException {
		IExtensionPointProxyFactory extensionsPointProxyFactory = getExtensionsPointProxyFactory();
		List<IJitArgsProviderExtension> jitArgsProviderExtensions = extensionsPointProxyFactory
				.createProxies(IJitArgsProviderExtension.class);

		if (jitArgsProviderExtensions.size() > 0) {
			return jitArgsProviderExtensions.get(0).createArgsProvider();
		}

		throw new CoreException(JitStatus.NO_JIT_ARGS_PROVIDER_AVAILABLE.getStatus());

	}

	public HsdisProvider getHsdisProvider() {
		IExtensionPointProxyFactory extensionsPointProxyFactory = getExtensionsPointProxyFactory();
		List<HsdisProviderExtension> hsdisProviderExtensions = extensionsPointProxyFactory
				.createProxies(HsdisProviderExtension.class);

		return new HsdisProvider() {

			@Override
			public Optional<File> getHsdisLibraryFolder(Env env) {
				Optional<File> hsdisLibraryFolder = Optional.empty();

				for (HsdisProviderExtension hsdisProviderExtension : hsdisProviderExtensions) {
					HsdisProvider provider = hsdisProviderExtension.getProvider();
					Optional<File> providerLibFolder = provider.getHsdisLibraryFolder(env);

					if (providerLibFolder.isPresent()) {
						hsdisLibraryFolder = providerLibFolder;
						break;
					}
				}

				return hsdisLibraryFolder;
			}
		};
	}

}
