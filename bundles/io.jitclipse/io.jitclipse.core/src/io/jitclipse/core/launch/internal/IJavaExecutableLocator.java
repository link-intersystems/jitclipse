package io.jitclipse.core.launch.internal;

import java.io.File;

import org.eclipse.jdt.launching.IVMInstall;

public interface IJavaExecutableLocator {

	public File locateExecutable(IVMInstall vmInstall);
}
