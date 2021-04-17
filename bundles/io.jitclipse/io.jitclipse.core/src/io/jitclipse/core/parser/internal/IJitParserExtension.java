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
package io.jitclipse.core.parser.internal;

import com.link_intersystems.eclipse.core.runtime.runtime.ConfigurationElement;
import com.link_intersystems.eclipse.core.runtime.runtime.ExecutableExtension;
import com.link_intersystems.eclipse.core.runtime.runtime.ExtensionPoint;

import io.jitclipse.core.JitCorePlugin;
import io.jitclipse.core.parser.IJitLogParser;

@ExtensionPoint(namespace = JitCorePlugin.ID, id = "parsers")
@ConfigurationElement("parser")
public interface IJitParserExtension {

	@ExecutableExtension("class")
	public IJitLogParser createParser();

}
