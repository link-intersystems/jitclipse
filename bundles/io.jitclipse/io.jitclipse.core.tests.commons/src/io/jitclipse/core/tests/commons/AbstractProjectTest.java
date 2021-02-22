package io.jitclipse.core.tests.commons;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.jupiter.api.BeforeEach;

public class AbstractProjectTest {

	protected IProject project;

	@BeforeEach
	public void setup() throws CoreException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();

		project = wsRoot.getProject("test-project-" + Integer.toHexString(System.identityHashCode(this)));

		if (!project.exists()) {
			project.create(null);
			project.open(null);
		}

	}

}