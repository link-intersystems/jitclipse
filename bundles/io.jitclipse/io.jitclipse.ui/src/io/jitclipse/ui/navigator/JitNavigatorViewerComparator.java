package io.jitclipse.ui.navigator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IPackage;

public class JitNavigatorViewerComparator extends org.eclipse.jface.viewers.ViewerComparator {

	private List<Class<?>> typeOrder = Arrays.asList(IPackage.class, IClass.class, IMethod.class);
	private LabelProvider labelProvider;

	public JitNavigatorViewerComparator() {
		labelProvider = new JitModelLabelProvider();
	}

	public JitNavigatorViewerComparator(Comparator<? super String> comparator) {
		super(comparator);
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int typeIndex1 = getTypeIndex(e1);
		int typeIndex2 = getTypeIndex(e2);

		int compare = Integer.compare(typeIndex1, typeIndex2);
		if (compare == 0) {
			compare = compareByLabels(e1, e2);
		}

		return compare;
	}

	private int compareByLabels(Object e1, Object e2) {
		String text1 = labelProvider.getText(e1);
		String text2 = labelProvider.getText(e2);

		if (text1 == null) {
			return 1;
		} else if (text2 == null) {
			return -1;
		}

		return text1.compareTo(text2);
	}

	private int getTypeIndex(Object e) {
		for (int i = 0; i < typeOrder.size(); i++) {
			Class<?> type = typeOrder.get(i);
			if (type.isInstance(e)) {
				return i;
			}
		}
		return 0;
	}

}
