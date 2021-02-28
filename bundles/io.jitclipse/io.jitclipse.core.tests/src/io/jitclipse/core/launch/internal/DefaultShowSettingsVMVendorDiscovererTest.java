package io.jitclipse.core.launch.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.launching.IVMInstall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.jitclipse.core.launch.VMVendor;

class DefaultShowSettingsVMVendorDiscovererTest  {

	private DefaultShowSettingsVMVendorDiscoverer vmVendorDiscoverer;
	private IVMInstall vmInstall;
	private IOutputGrabber outputGrabber;
	private VMSettingsOutputFixture vmSettingsOutputFixture;
	private IJavaExecutableLocator executableLocator;

	@BeforeEach
	public void setup() {
		vmInstall = Mockito.mock(IVMInstall.class);
		outputGrabber = Mockito.mock(IOutputGrabber.class);
		executableLocator = Mockito.mock(IJavaExecutableLocator.class);

		vmVendorDiscoverer = new DefaultShowSettingsVMVendorDiscoverer();
		vmVendorDiscoverer.setOutputGrabber(outputGrabber);
		vmVendorDiscoverer.setExecutableLocator(executableLocator);

		when(executableLocator.locateExecutable(vmInstall)).thenReturn(new File("java"));
		when(vmInstall.getInstallLocation()).thenReturn(new File(System.getProperty("java.home")));

		vmSettingsOutputFixture = new VMSettingsOutputFixture();
	}

	@Test
	void discoverUnknownVM() throws IOException {
		vmSettingsOutputFixture.mockUnknownVMOutput(outputGrabber);

		VMVendor vendor = vmVendorDiscoverer.discover(vmInstall);
		assertEquals(VMVendor.UNKNOWN, vendor);
	}
	@Test
	void discoverOracleVM() throws IOException {
		vmSettingsOutputFixture.mockOracleVMOutput(outputGrabber);

		VMVendor vendor = vmVendorDiscoverer.discover(vmInstall);
		assertEquals(VMVendor.ORACLE, vendor);
	}

}
