package io.jitclipse.core.tests.commons;

import static org.eclipse.core.resources.IResource.DEPTH_INFINITE;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.jupiter.api.BeforeEach;

public class AbstractProjectTest {

	protected IProject project;

	@BeforeEach
	public final void setupTestProject() throws CoreException {
		cleanWorkspace();

		String projectName = getProjectName();
		project = createProject(projectName);

		refresh();
	}

	protected void refresh() {
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(DEPTH_INFINITE, null);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	private void cleanWorkspace() throws CoreException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();
		IProject[] projects = wsRoot.getProjects();
		for (IProject project : projects) {
			project.delete(true, null);
		}

		refresh();
	}

	private IProject createProject(String projectName) throws CoreException {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();

		IProject project = wsRoot.getProject(projectName);

		if (!project.exists()) {
			project.create(null);
			project.open(null);
		}

		return project;
	}

	protected String getProjectName() {
		return "test-project-" + Integer.toHexString(System.identityHashCode(this));
	}

}