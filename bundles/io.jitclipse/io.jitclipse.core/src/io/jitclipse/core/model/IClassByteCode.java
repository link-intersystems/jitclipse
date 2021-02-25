package io.jitclipse.core.model;

import java.util.List;

public interface IClassByteCode extends List<IMemberByteCode> {

	IMemberByteCode getMemberBytecode(IMethod method);

	IClass getType();

}
