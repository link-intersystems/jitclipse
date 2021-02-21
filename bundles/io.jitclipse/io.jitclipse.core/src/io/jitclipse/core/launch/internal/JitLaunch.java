package io.jitclipse.core.launch.internal;

import static io.jitclipse.core.JitCorePlugin.LAUNCH_MODE;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.Launch;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import com.link_intersystems.eclipse.core.runtime.IPluginLog;

import io.jitclipse.core.launch.IJitLaunch;
import io.jitclipse.core.resources.IHotspotLogFile;
import io.jitclipse.core.resources.IHotspotLogFolder;
import io.jitclipse.core.resources.IJitProject;

public class JitLaunch extends Launch implements IJitLaunch {

	private static final String ATTR_HOTSPOT_LOG_FILE_LOCATION = IJitLaunch.class.getCanonicalName()
			+ ".hotspotLogFileLocation";

	private IFile hotspotLogFile;
	private IProject project;

	public JitLaunch(IPluginLog pluginLog, ILaunchConfiguration launchConfiguration) {
		super(launchConfiguration, LAUNCH_MODE, null);

		String projectName;
		try {
			projectName = launchConfiguration.getAttribute("org.eclipse.jdt.launching.PROJECT_ATTR", (String) null);
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			project = root.getProject(projectName);

			IJitProject jitProject = project.getAdapter(IJitProject.class);
			IHotspotLogFolder hotspotLogFolder = jitProject.getHotspotLogFolder();
			hotspotLogFile = hotspotLogFolder.newHotspotLogFile();

			setAttribute(ATTR_HOTSPOT_LOG_FILE_LOCATION, hotspotLogFile.getLocation().toOSString());
		} catch (CoreException e) {
			pluginLog.logError(e);
		}

	}

	@Override
	public IHotspotLogFile getHotspotLogFile() {
		try {
			hotspotLogFile.getParent().refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
		}
		return hotspotLogFile.getAdapter(IHotspotLogFile.class);
	}

	public ILaunchConfiguration getEffectiveLaunchConfiguration() throws CoreException {
		ILaunchConfiguration configuration = getLaunchConfiguration();
		String arguments = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, ""); //$NON-NLS-1$

		StringBuilder sb = new StringBuilder(arguments);

		ILaunchConfigurationWorkingCopy workingCopy = configuration.getWorkingCopy();

		if (hotspotLogFile != null) {
			JitArgs jitArgs = new OpenJDKJitArgs();

			jitArgs.setHotspotLogEnabled(true);
			IPath location = hotspotLogFile.getLocation();
			jitArgs.setHotspotLogFile(location.toFile());
			jitArgs.setDisassembledCodeEnabled(true);
			jitArgs.setClassModelEnabled(true);

			if (!jitArgs.isEmpty()) {
				sb.append(' ');
				sb.append(jitArgs);
			}
		}

		String jitEnabledArguments = sb.toString();

		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, jitEnabledArguments);

		return workingCopy;
	}

	@Override
	public IProject getProject() {
		return project;
	}

}
