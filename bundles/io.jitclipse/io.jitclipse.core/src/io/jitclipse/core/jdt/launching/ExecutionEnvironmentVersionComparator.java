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
package io.jitclipse.core.jdt.launching;

import java.text.MessageFormat;
import java.util.Comparator;

import org.eclipse.jdt.launching.environments.IExecutionEnvironment;

import com.link_intersystems.eclipse.core.runtime.runtime.IPluginLog;

import io.jitclipse.core.JitCorePlugin;

final class ExecutionEnvironmentVersionComparator implements Comparator<IExecutionEnvironment> {

	private IPluginLog pluginLog;

	public ExecutionEnvironmentVersionComparator() {
		this(JitCorePlugin.getInstance().getPluginLog());

	}

	public ExecutionEnvironmentVersionComparator(IPluginLog pluginLog) {
		this.pluginLog = pluginLog;
	}

	@Override
	public int compare(IExecutionEnvironment o1, IExecutionEnvironment o2) {
		String id1 = o1.getId();
		String id2 = o2.getId();

		String version1 = id1.substring(id1.indexOf('-') + 1);
		String version2 = id2.substring(id2.indexOf('-') + 1);

		try {
			double parseDouble1 = Double.parseDouble(version1);
			double parseDouble2 = Double.parseDouble(version2);
			return Double.compare(parseDouble1, parseDouble2);
		} catch (NumberFormatException e) {
			String msg = MessageFormat.format(
					"Unable to compare execution environment versions {0} <> {1}. Maybe the wrong environment version will be choosen.",
					id1, id2);
			pluginLog.logError(msg, e);
			return 0;
		}

	}
}