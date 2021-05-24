/*******************************************************************************
 * Copyright (c) 2021 Link Intersystems GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Link Intersystems GmbH - René Link - API and implementation
 *******************************************************************************/
package io.jitclipse.core.jdt.launching;

import java.io.File;

import org.eclipse.jdt.launching.IVMInstall;

public class DefaultJavaExecutableLocator implements IJavaExecutableLocator {

	private static final String JRE = "jre"; //$NON-NLS-1$

	private static final String[] fgCandidateJavaFiles = { "javaw", "javaw.exe", "java", "java.exe", "j9w", "j9w.exe", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"j9", "j9.exe" }; //$NON-NLS-1$ //$NON-NLS-2$
	private static final String[] fgCandidateJavaLocations = { File.separator, "bin" + File.separatorChar, //$NON-NLS-1$
			JRE + File.separatorChar + "bin" + File.separatorChar };//$NON-NLS-1$

	@Override
	public File locateExecutable(IVMInstall vmInstall) {
		return findJavaExecutable(vmInstall.getInstallLocation());
	}

	/**
	 * @param vmInstallLocation
	 * @return
	 */
	private File findJavaExecutable(File vmInstallLocation) {
		// Try each candidate in order. The first one found wins. Thus, the order
		// of fgCandidateJavaLocations and fgCandidateJavaFiles is significant.

		boolean isBin = false;
		String filePath = vmInstallLocation.getPath();
		int index = filePath.lastIndexOf(File.separatorChar);
		if (index > 0 && filePath.substring(index + 1).equals("bin")) { //$NON-NLS-1$
			isBin = true;
		}
		for (int i = 0; i < fgCandidateJavaFiles.length; i++) {
			for (int j = 0; j < fgCandidateJavaLocations.length; j++) {
				if (!isBin && j == 0) {
					// search in "." only under bin for java executables for Java 9 and above
					continue;
				}
				File javaFile = new File(vmInstallLocation, fgCandidateJavaLocations[j] + fgCandidateJavaFiles[i]);
				if (javaFile.isFile()) {
					return javaFile;
				}
			}
		}
		return null;
	}

}
