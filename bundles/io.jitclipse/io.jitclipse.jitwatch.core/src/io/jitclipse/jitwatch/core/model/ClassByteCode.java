package io.jitclipse.jitwatch.core.model;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adoptopenjdk.jitwatch.model.IMetaMember;
import org.adoptopenjdk.jitwatch.model.bytecode.ClassBC;
import org.adoptopenjdk.jitwatch.model.bytecode.MemberBytecode;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IClassByteCode;
import io.jitclipse.core.model.IMemberByteCode;
import io.jitclipse.core.model.IMethod;

public class ClassByteCode extends AbstractList<IMemberByteCode> implements IClassByteCode {

	private ModelContext modelContext;
	private ClassBC classBC;

	private Map<IMethod, IMemberByteCode> memberByteCodes = new HashMap<>();
	private IClass aClass;

	public ClassByteCode(ModelContext modelContext, ClassBC classBC, IClass aClass) {
		this.modelContext = modelContext;
		this.classBC = classBC;
		this.aClass = aClass;
	}

	@Override
	public IClass getType() {
		return aClass;
	}

	@Override
	public IMemberByteCode getMemberBytecode(IMethod method) {
		IMemberByteCode memberByteCode;
		if (memberByteCodes.containsKey(method)) {
			memberByteCode = memberByteCodes.get(method);
		} else {
			memberByteCode = doGetMemberByteCode(method);
			memberByteCodes.put(method, memberByteCode);
		}

		return memberByteCode;
	}

	private IMemberByteCode doGetMemberByteCode(IMethod method) {
		try {
			IMetaMember metaMember = modelContext.getJitModelElement(method);
			MemberBytecode memberBytecode = classBC.getMemberBytecode(metaMember);
			return modelContext.getMemberByteCode(memberBytecode);
		} catch (RuntimeException e) {
			return null;
		}
	}

	private List<IMemberByteCode> getMethodByteCodes() {
		List<MemberBytecode> memberBytecodeList = classBC.getMemberBytecodeList();
		return modelContext.getMemberByteCodes(memberBytecodeList);
	}

	@Override
	public IMemberByteCode get(int index) {
		return getMethodByteCodes().get(index);
	}

	@Override
	public int size() {
		return getMethodByteCodes().size();
	}

}
