package io.jitclipse.core;

import org.eclipse.core.runtime.CoreException;

import com.link_intersystems.eclipse.core.runtime.runtime.IPluginContext;

import io.jitclipse.core.parser.IJitLogParser;

public interface JitPluginContext extends IPluginContext {

	IJitLogParser getJitLogParser() throws CoreException;

}