package io.jitclipse.core.model.lock;

import java.util.AbstractList;
import java.util.List;

public class OptimizedLockList extends AbstractList<IOptimisedLock> implements IOptimisedLockList {

	private List<IOptimisedLock> optimizedLocks;

	public OptimizedLockList(List<IOptimisedLock> optimizedLocks) {
		this.optimizedLocks = optimizedLocks;
	}

	@Override
	public IOptimisedLock get(int index) {
		return optimizedLocks.get(index);
	}

	@Override
	public int size() {
		return optimizedLocks.size();
	}
}
