package io.jitclipse.core.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MethodList extends AbstractList<IMethod> implements IMethodList {

	private List<IMethod> methods = new ArrayList<>();

	public MethodList(List<IMethod> methods) {
		this.methods.addAll(methods);
	}

	@Override
	public Optional<IMethod> findByName(String name) {
		return find(c -> c.getName().equals(name));
	}

	@Override
	public Optional<IMethod> find(Predicate<IMethod> predicate) {
		return stream().filter(predicate).findFirst();
	}

	@Override
	public IMethod get(int index) {
		return methods.get(index);
	}

	@Override
	public int size() {
		return methods.size();
	}

}
