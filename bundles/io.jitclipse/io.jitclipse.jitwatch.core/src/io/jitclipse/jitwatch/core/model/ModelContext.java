package io.jitclipse.jitwatch.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.adoptopenjdk.jitwatch.model.IMetaMember;
import org.adoptopenjdk.jitwatch.model.JITDataModel;
import org.adoptopenjdk.jitwatch.model.MetaClass;
import org.adoptopenjdk.jitwatch.model.MetaPackage;
import org.adoptopenjdk.jitwatch.model.bytecode.BytecodeInstruction;
import org.adoptopenjdk.jitwatch.model.bytecode.ClassBC;
import org.adoptopenjdk.jitwatch.model.bytecode.MemberBytecode;

import io.jitclipse.core.model.IByteCodeInstruction;
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

	public ModelContext(JITDataModel jitDataModel) {
		this.jitDataModel = jitDataModel;
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

	private IClassByteCode createClassByteCode(ClassBC classBC, IClass aClass) {
		return new ClassByteCode(this, classBC, aClass);
	}

	private ICompilation createCompilation(org.adoptopenjdk.jitwatch.model.Compilation compilation) {
		return new Compilation(this, compilation);
	}

	public IClassByteCode getClassByteCode(MetaClass metaClass) {
		ClassBC classBytecode = metaClass.getClassBytecode(jitDataModel, getClassLocations());
		IClass aClass = getClass(metaClass);
		return getElement(classBytecode, cbc -> createClassByteCode(cbc, aClass));
	}

	private List<String> getClassLocations() {
		// TODO get from jit config
		return Arrays.asList();
	}

	public IMetaMember getJitModelElement(IMethod method) {
		return (IMetaMember) coreModelToJitWatch.get(method);
	}

	public IMemberByteCode getMemberByteCode(MemberBytecode memberBytecode) {
		return getElement(memberBytecode, this::createMemberByteCode);
	}

	public List<IMemberByteCode> getMemberByteCodes(List<MemberBytecode> memberBytecodeList) {
		return getList(memberBytecodeList, this::createMemberByteCode);
	}

	private IMemberByteCode createMemberByteCode(MemberBytecode memberBytecode) {
		return new MemberByteCode(this, memberBytecode);
	}

	public List<IByteCodeInstruction> getByteCodeInstructions(MemberBytecode memberBytecode) {
		List<BytecodeInstruction> instructions = memberBytecode.getInstructions();
		return getList(instructions, i -> createByteCodeInstructions(i, memberBytecode));
	}

	private IByteCodeInstruction createByteCodeInstructions(BytecodeInstruction bytecodeInstruction, MemberBytecode memberBytecode) {
		return new ByteCodeInstruction(this, bytecodeInstruction, memberBytecode);
	}

}
