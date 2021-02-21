package io.jitclipse.core.resources.internal;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.junit.jupiter.api.BeforeEach;

public class AbstractJavaProjectTest extends AbstractProjectTest {

	@BeforeEach
	public void setupJavaProject() throws CoreException {
		IProjectDescription description = project.getDescription();
		List<String> natures = Arrays.asList(description.getNatureIds());

		if (!natures.contains(JavaCore.NATURE_ID)) {
			initializeJavaProject();
		}
	}

	private void initializeJavaProject() throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = new String[] { JavaCore.NATURE_ID };
		description.setNatureIds(natures);
		project.setDescription(description, null);

		IJavaProject javaProject = JavaCore.create(project);
		IFolder binFolder = project.getFolder("bin");
		if (!binFolder.exists()) {
			binFolder.create(false, true, null);
		}
		javaProject.setOutputLocation(binFolder.getFullPath(), null);

		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
		for (LibraryLocation element : locations) {
			entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
		}
		// add libs to project class path
		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);

		IFolder sourceFolder = project.getFolder("src");
		if (!sourceFolder.exists()) {
			sourceFolder.create(false, true, null);
			IFile mainJavaFile = sourceFolder.getFile("Main.java");
			initializeMainClass(mainJavaFile);
		}

		IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(sourceFolder);
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaCore.newSourceEntry(root.getPath());
		javaProject.setRawClasspath(newEntries, null);

		project.build(IncrementalProjectBuilder.CLEAN_BUILD, null);

	}


	private void initializeMainClass(IFile mainJavaFile) throws CoreException {
		if (mainJavaFile.exists()) {
			return;
		}

		mainJavaFile.create(new ByteArrayInputStream(new String("public class Main {\r\n" //
				+ "\r\n" //
				+ "	public static void main(String[] args) {\r\n" //
				+ "		for (int i = 0; i < 100000; i++) {\r\n" //
				+ "			print(i);\r\n" //
				+ "		}\r\n" //
				+ "	}\r\n" //
				+ "\r\n" //
				+ "	public static synchronized void print(int i) {\r\n" //
				+ "		StringBuffer sb = new StringBuffer();\r\n" //
				+ "\r\n" //
				+ "		sb.append(\"test\");\r\n" //
				+ "		sb.append(\" \");\r\n" //
				+ "		sb.append(i);\r\n" //
				+ "\r\n" //
				+ "		System.out.println(sb.toString());\r\n" //
				+ "	}\r\n" //
				+ "}").getBytes()), false, null);
	}

}