package io.jitclipse.core.tests.commons;

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
	public final void setupJavaProject() throws CoreException {
		IProjectDescription description = project.getDescription();
		List<String> natures = Arrays.asList(description.getNatureIds());

		if (!natures.contains(JavaCore.NATURE_ID)) {
			initializeJavaProject();
		}

		refresh();
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
		List<IClasspathEntry> newEntries = new ArrayList<>(Arrays.asList(oldEntries));

		newEntries.add(JavaCore.newSourceEntry(root.getPath()));

		IFolder libsFolder = project.getFolder("libs");
		if (!libsFolder.exists()) {
			libsFolder.create(false, true, null);
			IFile commonsLangLib = libsFolder.getFile("commons-lang.jar");
			initializeCommonsLangLib(commonsLangLib);

			IClasspathEntry libraryEntry = JavaCore.newLibraryEntry(commonsLangLib.getLocation(), null, null);
			newEntries.add(libraryEntry);
		}

		javaProject.setRawClasspath(newEntries.toArray(new IClasspathEntry[newEntries.size()]), null);

		project.build(IncrementalProjectBuilder.CLEAN_BUILD, null);

	}

	private void initializeMainClass(IFile mainJavaFile) throws CoreException {
		if (mainJavaFile.exists()) {
			return;
		}

		mainJavaFile.create(getClass().getResourceAsStream("Main.java.txt"), false, null);
	}

	private void initializeCommonsLangLib(IFile commonsLangFile) throws CoreException {
		if (commonsLangFile.exists()) {
			return;
		}

		commonsLangFile.create(getClass().getResourceAsStream("commons-lang-2.6.jar"), false, null);
	}

}