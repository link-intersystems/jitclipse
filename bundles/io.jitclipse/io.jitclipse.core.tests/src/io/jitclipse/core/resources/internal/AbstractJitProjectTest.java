package io.jitclipse.core.resources.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.jupiter.api.BeforeEach;

public class AbstractJitProjectTest {

	protected IProject project;

	@BeforeEach
	public void setup() throws CoreException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		project = root.getProject("testProject");

		if (project.exists()) {
			project.delete(true, null);
		}

		project.create(null);
	}

}