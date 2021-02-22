package io.jitclipse.core.tests;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.junit.jupiter.api.BeforeEach;

import io.jitclipse.core.resources.IHotspotLogFolder;
import io.jitclipse.core.resources.IJitProject;

public class AbstractJitProjectTest extends AbstractJavaProjectTest {

	protected IHotspotLogFolder hotspotLogFolder;
	protected IJitProject jitProject;

	@BeforeEach
	public void setupJitProject() throws CoreException {
		jitProject = project.getAdapter(IJitProject.class);
		hotspotLogFolder = jitProject.getHotspotLogFolder();
		IFolder folder = hotspotLogFolder.getFolder();
		IFile hotspotLogFile = folder.getFile("hotspot.log");
		hotspotLogFile.create(AbstractJitProjectTest.class.getResourceAsStream("hotspot-example.log"), true, null);

		IFile otherFile = folder.getFile("other.log");
		otherFile.create(new ByteArrayInputStream(new byte[0]), true, null);
	}

}