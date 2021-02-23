package io.jitclipse.core.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface IMethodList extends List<IMethod> {

	default public boolean containsHotspots() {
		for (IMethod method : this) {
			if (method.isHot()) {
				return true;
			}
		}
		return false;
	}

	Optional<IMethod> findByName(String name);

	Optional<IMethod> find(Predicate<IMethod> predicate);

}
