package io.jitclipse.core.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ClassList extends AbstractList<IClass> implements IClassList {

	private List<IClass> classes = new ArrayList<>();

	public ClassList(List<IClass> classes) {
		this.classes.addAll(classes);
	}

	@Override
	public Optional<IClass> findClassByName(String fullQualifiedClassName) {
		return findClass(c -> c.getName().equals(fullQualifiedClassName));
	}

	@Override
	public Optional<IClass> findClass(Predicate<IClass> classPredicate) {
		return stream().filter(classPredicate).findFirst();
	}

	@Override
	public IClass get(int index) {
		return classes.get(index);
	}

	@Override
	public int size() {
		return classes.size();
	}

}
