package io.jitclipse.core.launch.internal;

import org.eclipse.jdt.launching.IVMInstall;

import io.jitclipse.core.launch.VMVendor;

public interface IVMVendorDiscoverer {

	public VMVendor discover(IVMInstall vmInstall);
}
