package io.jitclipse.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

import com.link_intersystems.eclipse.core.preferences.IPreferenceProxyFactory;
import com.link_intersystems.eclipse.ui.jface.plugin.AbstractJFacePlugin;

import io.jitclipse.ui.preferences.IJitPreferences;

public class JitUIPlugin extends AbstractJFacePlugin implements IJitUIPluginContext {

	public static final String ID = "io.jitclipse.ui";

	private static JitUIPlugin inst;

	private BundleContext context;

	public static JitUIPlugin getInstance() {
		return inst;
	}

	public JitUIPlugin() {
		super(ID);
	}

	public static IStatus errorStatus(String message, Throwable t) {
		return new Status(IStatus.ERROR, ID, IStatus.ERROR, message, t);
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		inst = this;
		context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		inst = null;
		context = null;
	}

	public BundleContext getContext() {
		return context;
	}

	public JitUIImages getJitUIImages() {
		return getImageResourcesProxy(JitUIImages.class);
	}

	public IJitPreferences getJitPreferences() {
		IPreferenceProxyFactory preferenceProxyFactory = getEclipsePreferenceProxyFactory();
		return preferenceProxyFactory.createProxy(IJitPreferences.class);
	}
}
