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
package io.jitclipse.ui.views.timeline;

import java.time.Duration;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.link_intersystems.eclipse.ui.nebula.graph.XYGraphViewer;

import io.jitclipse.core.model.IMethod;

public class TimelineViewer extends XYGraphViewer {

	private static class DurationLabelProvider extends BaseLabelProvider implements ILabelProvider {

		@Override
		public Image getImage(Object element) {
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof Double) {
				Double xValue = (Double) element;
				Duration duration = Duration.ofMillis(xValue.longValue());
				return humanReadableFormat(duration);
			}
			return null;
		}

		private static String humanReadableFormat(Duration duration) {
			StringBuilder sb = new StringBuilder();

			long days = duration.toDaysPart();
			if (days > 0) {
				sb.append(days);
				sb.append("d");
				sb.append(" ");
			}

			long hours = duration.toHoursPart();
			long minutes = duration.toMinutesPart();
			long seconds = duration.toSecondsPart();
			long millis = duration.toMillisPart();

			String format;
			if (millis == 0) {
				format = String.format("%02d:%02d:%02d", hours, minutes, seconds, millis);
			} else {
				format = String.format("%02d:%02d:%02d,%03d", hours, minutes, seconds, millis);
			}

			sb.append(format);

			return sb.toString();
		}

	}

	private IMethod highlightMethod;

	public TimelineViewer(Composite parent, int style) {
		super(parent, style);

		setContentProvider(new CompilationContentProvider());
		setxAxisLabelProvider(new DurationLabelProvider());
	}

	public void setHighlightMethod(IMethod highlightMethod) {
		this.highlightMethod = highlightMethod;
	}

	public IMethod getHighlightMethod() {
		return highlightMethod;
	}

}
