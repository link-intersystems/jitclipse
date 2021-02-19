package io.jitclipse.jitwatch.core.model;

import java.util.AbstractList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.jitclipse.core.model.ICompilation;
import io.jitclipse.core.model.ICompilationList;

public class CompilationList extends AbstractList<ICompilation> implements ICompilationList {

	public static class NMethodEmittedTimeComparator implements Comparator<ICompilation> {

		@Override
		public int compare(ICompilation o1, ICompilation o2) {
			long nMethodEmittedTime1 = o1.getId();
			long nMethodEmittedTime2 = o2.getId();

			return Long.compare(nMethodEmittedTime1, nMethodEmittedTime2);
		}

	}

	private List<ICompilation> compilations;

	public CompilationList(List<ICompilation> compilations) {
		this(compilations, new NMethodEmittedTimeComparator());
	}

	public CompilationList(List<ICompilation> compilations, Comparator<ICompilation> comparator) {
		this.compilations = compilations;
		Collections.sort(compilations, comparator);
	}

	@Override
	public ICompilation get(int index) {
		return compilations.get(index);
	}

	@Override
	public int size() {
		return compilations.size();
	}

}
