package io.jitclipse.core.model.allocation;

import java.util.AbstractList;
import java.util.List;

public class EliminatedAllocationList extends AbstractList<IEliminatedAllocation> implements IEliminatedAllocationList {

	private List<IEliminatedAllocation> eliminatedAllocations;

	public EliminatedAllocationList(List<IEliminatedAllocation> eliminatedAllocations) {
		this.eliminatedAllocations = eliminatedAllocations;
	}

	@Override
	public IEliminatedAllocation get(int index) {
		return eliminatedAllocations.get(index);
	}

	@Override
	public int size() {
		return eliminatedAllocations.size();
	}
}
