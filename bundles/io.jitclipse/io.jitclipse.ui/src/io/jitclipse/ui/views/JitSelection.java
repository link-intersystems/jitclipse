package io.jitclipse.ui.views;

import java.util.Objects;
import java.util.function.Consumer;

import org.eclipse.jface.viewers.ISelection;

import com.link_intersystems.eclipse.ui.jface.viewers.AdaptableSelectionList;
import com.link_intersystems.eclipse.ui.jface.viewers.SelectionList;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IHotspotLog;
import io.jitclipse.core.model.IMethod;
import io.jitclipse.core.model.IPackage;
import io.jitclipse.core.resources.IHotspotLogFile;

public class JitSelection {

	private IHotspotLog hotspotLog;
	private IPackage aPackage;
	private IClass type;
	private IMethod method;

	public JitSelection() {
	}

	public JitSelection(ISelection selection) {
		set(selection, IHotspotLog.class, this::setHotspotLog);
		set(selection, IHotspotLogFile.class, this::setHotspotLogFile);
		set(selection, IPackage.class, this::setPackage);
		set(selection, IClass.class, this::setType);
		set(selection, IMethod.class, this::setMethod);
	}

	private <T> void set(ISelection selection, Class<T> type, Consumer<T> setter) {
		SelectionList<T> elements = new AdaptableSelectionList<>(type, selection);
		T firstElement = elements.getFirstElement().orElse(null);
		setter.accept(firstElement);
	}

	private void setHotspotLogFile(IHotspotLogFile hotspotLogFile) {
		if (hotspotLogFile != null) {
			setHotspotLog(hotspotLogFile.getHotspotLog());
		}
	}

	private void setHotspotLog(IHotspotLog hotspotLog) {
		this.hotspotLog = hotspotLog;
	}

	private void setPackage(IPackage aPackage) {
		this.aPackage = aPackage;
	}

	private void setType(IClass type) {
		this.type = type;
	}

	private void setMethod(IMethod method) {
		this.method = method;
	}

	public IHotspotLog getHotspotLog() {
		return hotspotLog;
	}

	public IPackage getPackage() {
		return aPackage;
	}

	public IClass getType() {
		return type;
	}

	public IMethod getMethod() {
		return method;
	}

	@Override
	public int hashCode() {
		return Objects.hash(hotspotLog, aPackage, type, method);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JitSelection other = (JitSelection) obj;
		return Objects.equals(hotspotLog, other.hotspotLog) && //
				Objects.equals(aPackage, other.aPackage) && //
				Objects.equals(type, other.type) && //
				Objects.equals(method, other.method); //
	}

}
