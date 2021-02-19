package io.jitclipse.jitwatch.core.model;

import java.util.List;

import org.adoptopenjdk.jitwatch.model.MetaPackage;

import io.jitclipse.core.model.IClass;
import io.jitclipse.core.model.IPackage;

public class Package implements IPackage {

	private MetaPackage metaPackage;
	private ModelContext modelContext;

	public Package(ModelContext modelContext, MetaPackage metaPackage) {
		this.modelContext = modelContext;
		this.metaPackage = metaPackage;
	}

	@Override
	public String getName() {
		return metaPackage.getName();
	}

	@Override
	public IPackage getParent() {
		return modelContext.getPackage(metaPackage.getParentPackage());
	}

	@Override
	public List<IPackage> getPackages() {
		return modelContext.getPackages(metaPackage.getChildPackages());
	}

	@Override
	public List<IClass> getClasses() {
		return modelContext.getClasses(metaPackage.getPackageClasses());
	}

	@Override
	public String getSimpleName() {
		String name = metaPackage.getName();

		MetaPackage parentPackage = metaPackage.getParentPackage();
		if (parentPackage != null) {
			name = name.substring(parentPackage.getName().length() + 1);
		}

		return name;
	}

}
