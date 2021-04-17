/*******************************************************************************
 * Copyright (c) 2021 Link Intersystems GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Link Intersystems GmbH - René Link - API and implementation
 *******************************************************************************/
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
