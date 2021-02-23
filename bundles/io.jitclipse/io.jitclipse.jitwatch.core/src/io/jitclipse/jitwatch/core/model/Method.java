package io.jitclipse.jitwatch.core.model;

import java.util.List;

import org.adoptopenjdk.jitwatch.model.Compilation;
import org.adoptopenjdk.jitwatch.model.IMetaMember;
import org.adoptopenjdk.jitwatch.model.MetaClass;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IClassByteCode;
import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.IMemberByteCode;
import io.jitclipse.core.model.IMethod;

public class Method implements IMethod {

	private ModelContext modelContext;
	private IMetaMember metaMethod;

	public Method(ModelContext modelContext, IMetaMember metaMethod) {
		this.modelContext = modelContext;
		this.metaMethod = metaMethod;
	}

	@Override
	public String getName() {
		return metaMethod.getMemberName();
	}

	@Override
	public String toSignatureString() {
		return metaMethod.toStringUnqualifiedMethodName(false, true);
	}

	@Override
	public IClass getType() {
		return modelContext.getClass(metaMethod.getMetaClass());
	}

	@Override
	public boolean matches(String fullyQualifiedName) {
		MetaClass metaClass = metaMethod.getMetaClass();
		String reportFqcn = metaClass.getFullyQualifiedName();
		return fullyQualifiedName.equals(reportFqcn);
	}

	@Override
	public IMemberByteCode getMemberByteCode() {
		IClass type = getType();
		IClassByteCode byteCode = type.getByteCode();
		return byteCode.getMemberBytecode(this);
	}

	@Override
	public List<ICompilation> getCompilations() {
		List<Compilation> compilations = metaMethod.getCompilations();
		return modelContext.getCompilations(compilations);
	}

}
