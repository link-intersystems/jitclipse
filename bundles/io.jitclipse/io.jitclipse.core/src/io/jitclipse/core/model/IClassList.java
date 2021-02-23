package io.jitclipse.core.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface IClassList extends List<IClass> {

	Optional<IClass> findClassByName(String fullQualifiedClassName);

	Optional<IClass> findClass(Predicate<IClass> classPredicate);

}
