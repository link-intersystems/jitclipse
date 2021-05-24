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
package io.jitclipse.core.jdt.launch;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.mockito.Matchers;
import org.mockito.Mockito;

import io.jitclipse.core.launch.internal.IOutputGrabber;

public class VMSettingsOutputFixture {
	public void mockUnknownVMOutput(IOutputGrabber outputGrabber) throws IOException {
		when(outputGrabber.grabOutput(Mockito.any(File.class), Matchers.<String>anyVararg()))
				.thenReturn("Property settings:\r\n" + "    awt.toolkit = sun.awt.windows.WToolkit\r\n");

	}

	protected void mockOracleVMOutput(IOutputGrabber outputGrabber) throws IOException {
		when(outputGrabber.grabOutput(Mockito.any(File.class), Matchers.<String>anyVararg()))
				.thenReturn("Property settings:\r\n" + "    awt.toolkit = sun.awt.windows.WToolkit\r\n"
						+ "    file.encoding = Cp1252\r\n" + "    file.separator = \\\r\n"
						+ "    java.awt.graphicsenv = sun.awt.Win32GraphicsEnvironment\r\n"
						+ "    java.awt.printerjob = sun.awt.windows.WPrinterJob\r\n" + "    java.class.path = \r\n"
						+ "    java.class.version = 55.0\r\n" + "    java.home = C:\\dev\\java\\x64\\jdk-11.0.2\r\n"
						+ "    java.io.tmpdir = C:\\Users\\RENE~1.LIN\\AppData\\Local\\Temp\\\r\n"
						+ "    java.library.path = C:\\dev\\java\\x64\\jdk-11.0.2\\bin\r\n"
						+ "        C:\\Windows\\Sun\\Java\\bin\r\n" + "        C:\\Windows\\system32\r\n"
						+ "        C:\\Windows\r\n"
						+ "        C:/dev/IDE/link-intersystems/2020-09-R/jre/bin/server\r\n"
						+ "        C:/dev/IDE/link-intersystems/2020-09-R/jre/bin\r\n"
						+ "        C:\\Program Files (x86)\\VMware\\VMware Workstation\\bin\\\r\n"
						+ "        C:\\Windows\\system32\r\n" + "        C:\\Windows\r\n"
						+ "        C:\\Windows\\System32\\Wbem\r\n"
						+ "        C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\\r\n"
						+ "        C:\\Windows\\System32\\OpenSSH\\\r\n"
						+ "        C:\\Program Files (x86)\\Plantronics\\Spokes3G\\\r\n"
						+ "        C:\\Program Files\\Git\\cmd\r\n" + "        C:\\Program Files\\dotnet\\\r\n"
						+ "        C:\\Program Files\\Microsoft SQL Server\\130\\Tools\\Binn\\\r\n"
						+ "        C:\\Program Files\\Microsoft SQL Server\\Client SDK\\ODBC\\170\\Tools\\Binn\\\r\n"
						+ "        C:\\Program Files\\PuTTY\\\r\n"
						+ "        C:\\Program Files (x86)\\Common Files\\Acronis\\SnapAPI\\\r\n"
						+ "        C:\\dev\\maven\\current\\bin\r\n" + "        C:\\dev\\java\\current\\bin\r\n"
						+ "        C:\\Users\\rene.link\\AppData\\Local\\Microsoft\\WindowsApps\r\n"
						+ "        C:\\Users\\rene.link\\.dotnet\\tools\r\n" + "        \r\n"
						+ "        C:\\dev\\IDE\\link-intersystems\\2020-09-R\r\n" + "        \r\n" + "        .\r\n"
						+ "    java.runtime.name = Java(TM) SE Runtime Environment\r\n"
						+ "    java.runtime.version = 11.0.2+9-LTS\r\n"
						+ "    java.specification.name = Java Platform API Specification\r\n"
						+ "    java.specification.vendor = Oracle Corporation\r\n"
						+ "    java.specification.version = 11\r\n" + "    java.vendor = Oracle Corporation\r\n"
						+ "    java.vendor.url = http://java.oracle.com/\r\n"
						+ "    java.vendor.url.bug = http://bugreport.java.com/bugreport/\r\n"
						+ "    java.vendor.version = 18.9\r\n" + "    java.version = 11.0.2\r\n"
						+ "    java.version.date = 2019-01-15\r\n" + "    java.vm.compressedOopsMode = Zero based\r\n"
						+ "    java.vm.info = mixed mode\r\n"
						+ "    java.vm.name = Java HotSpot(TM) 64-Bit Server VM\r\n"
						+ "    java.vm.specification.name = Java Virtual Machine Specification\r\n"
						+ "    java.vm.specification.vendor = Oracle Corporation\r\n"
						+ "    java.vm.specification.version = 11\r\n" + "    java.vm.vendor = Oracle Corporation\r\n"
						+ "    java.vm.version = 11.0.2+9-LTS\r\n" + "    jdk.debug = release\r\n"
						+ "    line.separator = \\r \\n \r\n" + "    os.arch = amd64\r\n"
						+ "    os.name = Windows 10\r\n" + "    os.version = 10.0\r\n" + "    path.separator = ;\r\n"
						+ "    sun.arch.data.model = 64\r\n"
						+ "    sun.boot.library.path = C:\\dev\\java\\x64\\jdk-11.0.2\\bin\r\n"
						+ "    sun.cpu.endian = little\r\n" + "    sun.cpu.isalist = amd64\r\n"
						+ "    sun.desktop = windows\r\n" + "    sun.io.unicode.encoding = UnicodeLittle\r\n"
						+ "    sun.java.launcher = SUN_STANDARD\r\n" + "    sun.jnu.encoding = Cp1252\r\n"
						+ "    sun.management.compiler = HotSpot 64-Bit Tiered Compilers\r\n"
						+ "    sun.os.patch.level = \r\n" + "    user.country = DE\r\n"
						+ "    user.dir = C:\\dev\\IDE\\link-intersystems\\2020-09-R\r\n"
						+ "    user.home = C:\\Users\\rene.link\r\n" + "    user.language = de\r\n"
						+ "    user.name = rene.link\r\n" + "    user.script = \r\n" + "    user.timezone = \r\n"
						+ "    user.variant = \r\n" + "\r\n" + "java version \"11.0.2\" 2019-01-15 LTS\r\n"
						+ "Java(TM) SE Runtime Environment 18.9 (build 11.0.2+9-LTS)\r\n"
						+ "Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.2+9-LTS, mixed mode)\r\n" + "");
	}

}