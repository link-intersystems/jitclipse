package io.jitclipse.jitwatch.core.model;

import java.util.List;

import org.adoptopenjdk.jitwatch.model.MetaClass;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IClassByteCode;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IMethodList;
import io.jitclipse.core.model.IPackage;
import io.jitclipse.core.model.MethodList;

public class Class implements IClass {

	private ModelContext modelContext;
	private MetaClass metaClass;
	private IClassByteCode classByteCode;

	public Class(ModelContext modelContext, MetaClass metaClass) {
		this.modelContext = modelContext;
		this.metaClass = metaClass;
	}

	@Override
	public String getName() {
		return metaClass.getFullyQualifiedName();
	}

	@Override
	public IPackage getPackage() {
		return modelContext.getPackage(metaClass.getPackage());
	}

	@Override
	public IMethodList getMethods() {
		List<IMethod> methods = modelContext.getMethods(metaClass.getMetaMembers());
		return new MethodList(methods);
	}

	@Override
	public String getSimpleName() {
		return metaClass.getName();
	}

	@Override
	public IClassByteCode getByteCode() {
		if (classByteCode == null) {
			classByteCode = modelContext.getClassByteCode(metaClass);
		}
		return classByteCode;
	}

}
