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
package io.jitclipse.ui.navigator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IPackage;
import io.jitclipse.ui.JitUIImages;
import io.jitclipse.ui.JitUIPlugin;

public class JitModelLabelProvider extends LabelProvider {

	private Map<Class<?>, Supplier<Image>> imageKeysByClass = new LinkedHashMap<>();

	public JitModelLabelProvider() {
		this(JitUIPlugin.getInstance().getJitUIImages());
	}

	public JitModelLabelProvider(JitUIImages jitUIImages) {
		imageKeysByClass.put(IPackage.class, jitUIImages::getPackageImage);
		imageKeysByClass.put(IClass.class, jitUIImages::getClassImage);
		imageKeysByClass.put(IMethod.class, jitUIImages::getMethodImage);
	}

	@Override
	public String getText(Object element) {
		String text = null;

		if (IPackage.class.isInstance(element)) {
			IPackage packageObj = IPackage.class.cast(element);
			text = packageObj.getSimpleName();
		} else if (IClass.class.isInstance(element)) {
			IClass clazz = IClass.class.cast(element);
			text = clazz.getSimpleName();
		} else if (IMethod.class.isInstance(element)) {
			IMethod metaMember = IMethod.class.cast(element);
			text = metaMember.toSignatureString();
		}

		return text;
	}

	@Override
	public Image getImage(Object element) {
		Set<Entry<Class<?>, Supplier<Image>>> entrySet = imageKeysByClass.entrySet();

		for (Entry<Class<?>, Supplier<Image>> entry : entrySet) {
			if (entry.getKey().isInstance(element)) {
				Supplier<Image> imageSupplier = entry.getValue();
				return imageSupplier.get();
			}
		}

		return super.getImage(element);
	}
}
