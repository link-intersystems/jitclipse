package io.jitclipse.core.launch.internal;

import static org.eclipse.debug.core.DebugEvent.TERMINATE;

import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchDelegate;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.eclipse.osgi.util.NLS;

import com.link_intersystems.eclipse.core.runtime.IExtensionPointProxyFactory;
import com.link_intersystems.eclipse.core.runtime.IPluginLog;

import io.jitclipse.core.CoreMessages;
import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.JitStatus;
import io.jitclipse.core.launch.IJitLaunch;
import io.jitclipse.core.resources.IHotspotLogFile;

public class JitLauncherDelegate implements ILaunchConfigurationDelegate2, IDebugEventSetListener {

	private static final String LAUNCH_DELEGATE_TYPE = "org.eclipse.jdt.launching.localJavaApplication";

	private static final String MODE = "run";

	private IPluginLog pluginLog;

	public JitLauncherDelegate() {
		this(JitCorePlugin.getInstance().getPluginLog());
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
			return launchDelegate.showCommandLine(effectiveConfiguration, MODE, jitLaunch, monitor);
		} finally {
			monitor.done();
		}
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
		launchDelegate.launch(effectiveConfiguration, MODE, jitLaunch, SubMonitor.convert(monitor, 1));

		monitor.done();
	}

	public JitLauncherDelegate(IPluginLog pluginLog) {
		this.pluginLog = pluginLog;
		DebugPlugin debugPlugin = DebugPlugin.getDefault();
		debugPlugin.addDebugEventListener(this);
	}

	private ILaunchConfigurationDelegate getLaunchDelegate(String launchtype) throws CoreException {
		ILaunchConfigurationType type = DebugPlugin.getDefault().getLaunchManager()
				.getLaunchConfigurationType(launchtype);
		if (type == null) {
			throw new CoreException(JitStatus.UNKOWN_LAUNCH_TYPE_ERROR.getStatus(launchtype));
		}

		ILaunchDelegate[] delegates = type.getDelegates(Collections.singleton(MODE));
		for (ILaunchDelegate delegate : delegates) {
			ILaunchConfigurationDelegate configurationDelegate = delegate.getDelegate();
			if (configurationDelegate instanceof JavaLaunchDelegate) {
				return configurationDelegate;
			}
		}

		throw new CoreException(JitStatus.UNKOWN_LAUNCH_TYPE_ERROR.getStatus(launchtype));
	}

	private ILaunchConfigurationDelegate getLaunchDelegate() throws CoreException {
		return getLaunchDelegate(LAUNCH_DELEGATE_TYPE);
	}

	public ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
		return new JitLaunch(pluginLog, configuration);
	}

	public boolean buildForLaunch(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		ILaunchConfigurationDelegate launchDelegate = getLaunchDelegate();
		if (launchDelegate instanceof ILaunchConfigurationDelegate2) {
			return ((ILaunchConfigurationDelegate2) launchDelegate).buildForLaunch(configuration, MODE, monitor);
		} else {
			return true;
		}
	}

	public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		ILaunchConfigurationDelegate launchDelegate = getLaunchDelegate();
		if (launchDelegate instanceof ILaunchConfigurationDelegate2) {
			return ((ILaunchConfigurationDelegate2) launchDelegate).preLaunchCheck(configuration, MODE, monitor);
		} else {
			return true;
		}
	}

	public boolean finalLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		ILaunchConfigurationDelegate launchDelegate = getLaunchDelegate();
		if (launchDelegate instanceof ILaunchConfigurationDelegate2) {
			return ((ILaunchConfigurationDelegate2) launchDelegate).finalLaunchCheck(configuration, MODE, monitor);
		} else {
			return true;
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

	private void refresh(IFile file) {
		try {
			file.getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			pluginLog.logError(e);
		}
	}

	protected JitLaunchListenerDelegate getLaunchResultListenerDelegate() {
		IExtensionPointProxyFactory extensionsPointProxyFactory = JitCorePlugin.getInstance()
				.getExtensionsPointProxyFactory();
		return new JitLaunchListenerDelegate(extensionsPointProxyFactory);
	}

}
