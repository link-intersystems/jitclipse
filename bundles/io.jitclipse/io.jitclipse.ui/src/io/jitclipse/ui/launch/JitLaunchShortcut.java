package io.jitclipse.ui.launch;

import static io.jitclipse.core.JitCorePlugin.LAUNCH_MODE;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;

import com.link_intersystems.eclipse.core.runtime.runtime.IPluginLog;

import io.jitclipse.ui.JitUIPlugin;

public class JitLaunchShortcut implements ILaunchShortcut, IExecutableExtension {

	private String delegateId;
	private ILaunchShortcut delegate;
	private IPluginLog pluginLog;

	public JitLaunchShortcut() {
		this(JitUIPlugin.getInstance().getPluginLog());
	}

	public JitLaunchShortcut(IPluginLog pluginLog) {
		this.pluginLog = pluginLog;
	}

	private ILaunchShortcut getDelegate() {
		if (delegate == null) {
			IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
					.getExtensionPoint(IDebugUIConstants.PLUGIN_ID, IDebugUIConstants.EXTENSION_POINT_LAUNCH_SHORTCUTS);
			for (final IConfigurationElement config : extensionPoint.getConfigurationElements()) {
				if (delegateId.equals(config.getAttribute("id"))) { //$NON-NLS-1$
					try {
						delegate = (ILaunchShortcut) config.createExecutableExtension("class"); //$NON-NLS-1$
					} catch (CoreException e) {
					}
					break;
				}
			}
			if (delegate == null) {
				String msg = "ILaunchShortcut declaration not found: " + delegateId; //$NON-NLS-1$
				pluginLog.logError(msg);
			}
		}
		return delegate;
	}

	public void launch(ISelection selection, String mode) {
		ILaunchShortcut delegate = getDelegate();
		if (delegate != null) {
			delegate.launch(selection, LAUNCH_MODE);
		}
	}

	public void launch(IEditorPart editor, String mode) {
		ILaunchShortcut delegate = getDelegate();
		if (delegate != null) {
			delegate.launch(editor, LAUNCH_MODE);
		}
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		delegateId = String.valueOf(data);
	}

}
