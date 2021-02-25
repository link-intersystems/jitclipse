package io.jitclipse.ui.bytecode.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IClassByteCode;

public class BytecodeEditorInput implements IEditorInput {

	private IClassByteCode classByteCode;

	public BytecodeEditorInput(IClassByteCode classByteCode) {
		this.classByteCode = classByteCode;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if(IClassByteCode.class.equals(adapter)) {
			return adapter.cast(classByteCode);
		}
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		IClass aClass = classByteCode.getType();
		return aClass.getSimpleName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		IClass aClass = classByteCode.getType();
		return aClass.getName();
	}

}
