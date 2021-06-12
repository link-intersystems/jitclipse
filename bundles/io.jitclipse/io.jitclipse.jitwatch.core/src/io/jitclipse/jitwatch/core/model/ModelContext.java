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
package io.jitclipse.jitwatch.core.model;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.adoptopenjdk.jitwatch.core.JITWatchConfig;
import org.adoptopenjdk.jitwatch.model.IMetaMember;
import org.adoptopenjdk.jitwatch.model.JITDataModel;
import org.adoptopenjdk.jitwatch.model.MetaClass;
import org.adoptopenjdk.jitwatch.model.MetaPackage;
import org.adoptopenjdk.jitwatch.model.assembly.AssemblyMethod;
import org.adoptopenjdk.jitwatch.model.bytecode.ClassBC;
import org.adoptopenjdk.jitwatch.model.bytecode.MemberBytecode;

import io.jitclipse.core.model.IAssembly;
import io.jitclipse.core.model.IAssemblyBlock;
import io.jitclipse.core.model.IAssemblyInstruction;
import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IClassByteCode;
import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.IMemberByteCode;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IPackage;

public class ModelContext {

	private Map<Object, Object> jitWatchToCoreModel = new IdentityHashMap<>();
	private Map<Object, Object> coreModelToJitWatch = new IdentityHashMap<>();
	private JITDataModel jitDataModel;
	private JITWatchConfig config;

	public ModelContext(JITDataModel jitDataModel, JITWatchConfig config) {
		this.jitDataModel = jitDataModel;
		this.config = config;
	}

	public List<ICompilation> getCompilations(List<org.adoptopenjdk.jitwatch.model.Compilation> compilations) {
		return getList(compilations, this::createCompilation);
	}

	public List<IMethod> getMethods(List<IMetaMember> metaMembers) {
		return getList(metaMembers, this::createMethod);
	}

	public IMethod getMethod(IMetaMember method) {
		return getElement(method, this::createMethod);
	}

	public IClass getClass(MetaClass metaClass) {
		return getElement(metaClass, this::createClass);
	}

	public List<IClass> getClasses(List<MetaClass> metaClasses) {
		return getList(metaClasses, this::createClass);
	}

	public List<IPackage> getPackages(List<MetaPackage> rootMetaPackages) {
		return getList(rootMetaPackages, this::createPackage);
	}

	public IPackage getPackage(MetaPackage metaPackage) {
		return getElement(metaPackage, this::createPackage);
	}

	private <T, R> List<R> getList(List<T> metaModelElements, Function<T, R> hotspotModelFactory) {
		List<R> hotspotModelElements = new ArrayList<>();

		for (T metaModelElement : metaModelElements) {
			R hotspotModelElement = getElement(metaModelElement, hotspotModelFactory);
			hotspotModelElements.add(hotspotModelElement);
		}

		return hotspotModelElements;
	}

	@SuppressWarnings("unchecked")
	private <T, R> R getElement(T metaModelElement, Function<T, R> hotspotModelFactory) {
		if (metaModelElement == null) {
			return null;
		}

		R hotspotModelElement = (R) jitWatchToCoreModel.get(metaModelElement);
		if (hotspotModelElement == null) {
			hotspotModelElement = hotspotModelFactory.apply(metaModelElement);
			jitWatchToCoreModel.put(metaModelElement, hotspotModelElement);
			coreModelToJitWatch.put(hotspotModelElement, metaModelElement);
		}
		return hotspotModelElement;
	}

	private IPackage createPackage(MetaPackage metaPackage) {
		return new Package(this, metaPackage);
	}

	private IClass createClass(MetaClass metaClass) {
		return new Class(this, metaClass);
	}

	private IMethod createMethod(IMetaMember metaMethod) {
		return new Method(this, metaMethod);
	}

	private IClassByteCode createClassByteCode(ClassBC classBC) {
		return new ClassByteCode(this, classBC);
	}

	private ICompilation createCompilation(org.adoptopenjdk.jitwatch.model.Compilation compilation) {
		return new Compilation(this, compilation);
	}

	public IClassByteCode getClassByteCode(MetaClass metaClass) {
		ClassBC classBytecode = metaClass.getClassBytecode(jitDataModel, getClassLocations());
		return getElement(classBytecode, this::createClassByteCode);
	}

	private List<String> getClassLocations() {
		return config.getAllClassLocations();
	}

	public IMetaMember getJitModelElement(IMethod method) {
		return (IMetaMember) coreModelToJitWatch.get(method);
	}

	public IMemberByteCode getMemberByteCode(MemberBytecode memberBytecode) {
		return getElement(memberBytecode, this::createMemberByteCode);
	}

	private IMemberByteCode createMemberByteCode(MemberBytecode memberBytecode) {
		return new MemberByteCode(this, memberBytecode);
	}

	public IAssembly getAssembly(AssemblyMethod assembly) {
		return getElement(assembly, this::createAssembly);
	}

	private IAssembly createAssembly(AssemblyMethod assemblyMethod) {
		return new Assembly(this, assemblyMethod);
	}

	public List<IAssemblyBlock> getAssemblyBlocks(List<org.adoptopenjdk.jitwatch.model.assembly.AssemblyBlock> blocks) {
		return getList(blocks, this::createAssemblyBlock);
	}

	private IAssemblyBlock createAssemblyBlock(org.adoptopenjdk.jitwatch.model.assembly.AssemblyBlock assemblyBlock) {
		return new AssemblyBlock(this, assemblyBlock);
	}

	public List<IAssemblyInstruction> getAssemblyInstructions(List<org.adoptopenjdk.jitwatch.model.assembly.AssemblyInstruction> instructions) {
		return getList(instructions, this::createAssemblyInstruction);
	}

	private IAssemblyInstruction createAssemblyInstruction(org.adoptopenjdk.jitwatch.model.assembly.AssemblyInstruction assemblyInstruction) {
		return new AssemblyInstruction(this, assemblyInstruction);
	}

}
