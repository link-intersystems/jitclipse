package io.jitclipse.core.model;

import java.util.List;

import com.link_intersystems.eclipse.core.runtime.runtime.IAdaptable2;

public interface IMethod extends IAdaptable2 {

	public String getName();

	public String toSignatureString();

	public IClass getType();

	boolean matches(String fullyQualifiedName);

	public IMemberByteCode getMemberByteCode();

	public List<ICompilation> getCompilations();

	default boolean isHot() {
		return !getCompilations().isEmpty();
	}

}
