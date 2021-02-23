package io.jitclipse.core.tests.commons;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.junit.jupiter.api.BeforeEach;

import io.jitclipse.core.resources.IHotspotLogFile;
import io.jitclipse.core.resources.IHotspotLogFolder;
import io.jitclipse.core.resources.IJitProject;

public class AbstractJitProjectTest extends AbstractJavaProjectTest {

	protected IHotspotLogFolder hotspotLogFolder;
	protected IJitProject jitProject;

	@BeforeEach
	public final void setupJitProject() throws CoreException {
		jitProject = project.getAdapter(IJitProject.class);
		hotspotLogFolder = jitProject.getHotspotLogFolder();
		IFolder folder = hotspotLogFolder.getFolder();
		addHostspotLogFile(AbstractJitProjectTest.class.getResourceAsStream("hotspot-example.log"), "hotspot.log");

		IFile otherFile = folder.getFile("other.log");
		otherFile.create(new ByteArrayInputStream(new byte[0]), true, null);
	}

	protected void addHostspotLogFile(String testRelativeFileName) throws CoreException {
		addHostspotLogFile(getClass().getResourceAsStream(testRelativeFileName), testRelativeFileName);
	}

	protected void addHostspotLogFile(String testRelativeFileName, String name) throws CoreException {
		addHostspotLogFile(getClass().getResourceAsStream(testRelativeFileName), name);
	}

	protected void addHostspotLogFile(InputStream in, String name) throws CoreException {
		if (!IHotspotLogFile.isHotspotLogFilename(name)) {
			throw new IllegalArgumentException("Not a hotspot log filename: " + name);
		}

		hotspotLogFolder = jitProject.getHotspotLogFolder();
		IFolder folder = hotspotLogFolder.getFolder();
		IFile hotspotLogFile = folder.getFile(name);
		hotspotLogFile.create(in, true, null);
	}

}