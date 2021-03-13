package io.jitclipse.core.launch.internal;

import static org.eclipse.core.resources.IResource.DEPTH_INFINITE;
import static org.eclipse.debug.core.DebugEvent.TERMINATE;

import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.osgi.util.NLS;

import com.link_intersystems.eclipse.core.runtime.runtime.IExtensionPointProxyFactory;
import com.link_intersystems.eclipse.core.runtime.runtime.IPluginLog;

import io.jitclipse.core.CoreMessages;
import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.JitStatus;
import io.jitclipse.core.launch.IJitArgsProvider;
import io.jitclipse.core.launch.IJitLaunch;
import io.jitclipse.core.resources.IHotspotLogFile;

public class JitLauncherDelegate
		implements ILaunchConfigurationDelegate2, IDebugEventSetListener, IExecutableExtension {

	public static final String DELEGATELAUNCHMODE = ILaunchManager.RUN_MODE;

	protected ILaunchConfigurationDelegate launchdelegate;

	protected ILaunchConfigurationDelegate2 launchdelegate2;

	private IPluginLog pluginLog;

	public JitLauncherDelegate() {
		this(JitCorePlugin.getInstance().getPluginLog());
	}

	public JitLauncherDelegate(IPluginLog pluginLog) {
		this.pluginLog = pluginLog;
		DebugPlugin debugPlugin = DebugPlugin.getDefault();
		debugPlugin.addDebugEventListener(this);
	}

	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		final String launchtype = config.getAttribute("type"); //$NON-NLS-1$
		launchdelegate = getLaunchDelegate(launchtype);
		if (launchdelegate instanceof ILaunchConfigurationDelegate2) {
			launchdelegate2 = (ILaunchConfigurationDelegate2) launchdelegate;
		}
	}

	@Override
	public String showCommandLine(ILaunchConfiguration configuration, String mode, ILaunch launch,
			IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		try {
			JitLaunch jitLaunch = (JitLaunch) launch;
			ILaunchConfiguration effectiveConfiguration = jitLaunch.getEffectiveLaunchConfiguration();

			ILaunchConfigurationDelegate launchDelegate = getLaunchDelegate();
			return launchDelegate.showCommandLine(effectiveConfiguration, DELEGATELAUNCHMODE, jitLaunch, monitor);
		} finally {
			monitor.done();
		}
	}

	private ILaunchConfigurationDelegate getLaunchDelegate(String launchtype) throws CoreException {
		ILaunchConfigurationType type = DebugPlugin.getDefault().getLaunchManager()
				.getLaunchConfigurationType(launchtype);
		if (type == null) {
			throw new CoreException(JitStatus.UNKOWN_LAUNCH_TYPE_ERROR.getStatus(launchtype));
		}
		return type.getDelegates(Collections.singleton(DELEGATELAUNCHMODE))[0].getDelegate();
	}

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(NLS.bind(CoreMessages.Launching_task, configuration.getName()), 2);

		if (monitor.isCanceled()) {
			return;
		}

		JitLaunch jitLaunch = (JitLaunch) launch;
		ILaunchConfiguration effectiveConfiguration = jitLaunch.getEffectiveLaunchConfiguration();

		ILaunchConfigurationDelegate launchDelegate = getLaunchDelegate();
		launchDelegate.launch(effectiveConfiguration, DELEGATELAUNCHMODE, jitLaunch, SubMonitor.convert(monitor, 1));

		monitor.done();
	}

	private ILaunchConfigurationDelegate getLaunchDelegate() throws CoreException {
		return launchdelegate;
	}

	public ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
		IJitArgsProvider argsProvider = getArgsProvider();
		return new JitLaunch(configuration, argsProvider, pluginLog);
	}

	private IJitArgsProvider getArgsProvider() throws CoreException {
		return JitCorePlugin.getInstance().getJitArgsProvider();
	}

	public boolean buildForLaunch(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		if (launchdelegate2 == null) {
			return true;
		} else {
			return launchdelegate2.buildForLaunch(configuration, DELEGATELAUNCHMODE, monitor);
		}
	}

	public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		if (launchdelegate2 == null) {
			return true;
		} else {
			return launchdelegate2.preLaunchCheck(configuration, DELEGATELAUNCHMODE, monitor);
		}
	}

	public boolean finalLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		if (launchdelegate2 == null) {
			return true;
		} else {
			return launchdelegate2.finalLaunchCheck(configuration, DELEGATELAUNCHMODE, monitor);
		}
	}

	@Override
	public void handleDebugEvents(DebugEvent[] events) {
		for (DebugEvent debugEvent : events) {
			handleDebugEvent(debugEvent);
		}
	}

	private void handleDebugEvent(DebugEvent debugEvent) {
		int kind = debugEvent.getKind();
		if (TERMINATE == kind) {
			handleTerminated(debugEvent);
		}
	}

	private void handleTerminated(DebugEvent debugEvent) {
		IProcess process = (IProcess) debugEvent.getSource();
		ILaunch launch = process.getLaunch();
		if (IJitLaunch.class.isInstance(launch)) {
			IJitLaunch jitLaunch = IJitLaunch.class.cast(launch);

			IHotspotLogFile hotspotLogfile = jitLaunch.getHotspotLogFile();
			IFile file = hotspotLogfile.getFile();
			refresh(file);

			JitLaunchListenerDelegate jitLaunchResultListenerDelegate = getLaunchResultListenerDelegate();
			jitLaunchResultListenerDelegate.finished(jitLaunch);

		}
	}

	protected JitLaunchListenerDelegate getLaunchResultListenerDelegate() {
		IExtensionPointProxyFactory extensionsPointProxyFactory = JitCorePlugin.getInstance()
				.getExtensionsPointProxyFactory();
		return new JitLaunchListenerDelegate(extensionsPointProxyFactory);
	}

	private void refresh(IFile file) {
		try {
			file.getProject().refreshLocal(DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			pluginLog.logError(e);
		}
	}

}
