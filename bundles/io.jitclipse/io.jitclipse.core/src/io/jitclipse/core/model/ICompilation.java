package io.jitclipse.core.model;

public interface ICompilation {

	long getNMethodEmittedTime();

	long getId();

	long getQueudTimestamp();

	long getStartTimestamp();

	JitCompiler getCompiler();

	public IMethod getMethod();

}
