package io.jitclipse.jitwatch.core.model;

import org.adoptopenjdk.jitwatch.model.IMetaMember;

import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.JitCompiler;

public class Compilation implements ICompilation {

	private ModelContext modelContext;
	private org.adoptopenjdk.jitwatch.model.Compilation compilation;

	public Compilation(ModelContext modelContext, org.adoptopenjdk.jitwatch.model.Compilation compilation) {
		this.modelContext = modelContext;
		this.compilation = compilation;
	}

	@Override
	public JitCompiler getCompiler() {
		String compiler = compilation.getCompiler();
		return JitCompiler.valueOf(compiler);
	}

	org.adoptopenjdk.jitwatch.model.Compilation getJitWatchCompilation() {
		return compilation;
	}

	public long getNMethodEmittedTime() {
		return compilation.getStampNMethodEmitted();
	}

	@Override
	public long getId() {
		return Long.valueOf(compilation.getCompileID());
	}

	@Override
	public long getQueudTimestamp() {
		return compilation.getStampTaskQueued();
	}

	@Override
	public long getStartTimestamp() {
		return compilation.getStampTaskCompilationStart();
	}

	@Override
	public IMethod getMethod() {
		IMetaMember member = compilation.getMember();
		return modelContext.getMethod(member);
	}

}
