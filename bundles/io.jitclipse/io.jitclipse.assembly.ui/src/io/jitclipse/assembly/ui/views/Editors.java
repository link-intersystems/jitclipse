package io.jitclipse.assembly.ui.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IDisposable;

public class Editors implements IDisposable {

	private static final String ALL_IDS = "";

	private class PartListenerAdapter implements IPartListener2 {

		private IEditorReference latestEditorReference;

		@Override
		public void partActivated(IWorkbenchPartReference partRef) {
			if (partRef instanceof IEditorReference) {
				IEditorReference editorReference = (IEditorReference) partRef;

				boolean editorReferenceChanged = !Objects.equals(editorReference, latestEditorReference);
				this.latestEditorReference = editorReference;

				String editorId = editorReference.getId();
				List<IEditorListener> editorListeners = getEditorListeners(editorId);

				editorListeners.forEach(el -> el.editorActivated(editorReference));

				if (editorReferenceChanged) {
					editorListeners.forEach(el -> el.editorChanged(editorReference));
				}
			}
		}

		@Override
		public void partClosed(IWorkbenchPartReference partRef) {
			if (partRef == latestEditorReference) {
				IEditorReference editorReference = (IEditorReference) partRef;
				String editorId = editorReference.getId();
				List<IEditorListener> editorListeners = getEditorListeners(editorId);
				editorListeners.forEach(el -> el.editorChanged(null));
			}
		}

		@Override
		public void partDeactivated(IWorkbenchPartReference partRef) {
			if (partRef instanceof IEditorReference) {
				IEditorReference editorReference = (IEditorReference) partRef;
				String editorId = editorReference.getId();
				List<IEditorListener> editorListeners = getEditorListeners(editorId);
				editorListeners.forEach(el -> el.editorDeactivated(editorReference));
			}
		}
	}

	private PartListenerAdapter partListenerAdapter = new PartListenerAdapter();

	private IWorkbenchPage registeredPage;

	private Map<String, List<IEditorListener>> editorListenersById = new HashMap<>();

	public Editors() {
	}

	public void init() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null) {
			registeredPage = activeWorkbenchWindow.getActivePage();
			if (registeredPage != null) {
				registeredPage.addPartListener(partListenerAdapter);
			}
		}
	}

	@Override
	public void dispose() {
		if (registeredPage != null) {
			registeredPage.removePartListener(partListenerAdapter);
		}
		registeredPage = null;
	}

	private List<IEditorListener> getEditorListeners(String editorId) {
		List<IEditorListener> editorListeners = new ArrayList<>();

		if (!ALL_IDS.equals(editorId)) {
			List<IEditorListener> allIdListeners = editorListenersById.getOrDefault(ALL_IDS, Collections.emptyList());
			editorListeners.addAll(allIdListeners);
		}

		editorListeners.addAll(editorListenersById.getOrDefault(editorId, Collections.emptyList()));

		return editorListeners;
	}

	public void addEditorListener(IEditorListener editorListener) {
		addEditorListener(ALL_IDS, editorListener);
	}

	public void addEditorListener(String editorId, IEditorListener editorListener) {
		List<IEditorListener> editorListeners = editorListenersById.computeIfAbsent(editorId, k -> new LinkedList<>());
		editorListeners.add(editorListener);
	}

	public void removeEditorListener(IEditorListener editorListener) {
		removeEditorListener(ALL_IDS, editorListener);
	}

	public void removeEditorListener(String editorId, IEditorListener editorListener) {
		List<IEditorListener> editorListeners = editorListenersById.get(editorId);
		if (editorListeners != null) {
			editorListeners.remove(editorListener);
		}

		if (editorListeners.isEmpty()) {
			editorListenersById.remove(editorId);
		}
	}

}
