package io.jitclipse.core.model;

import java.util.ArrayList;
import java.util.List;

public class TestPackage implements IPackage {

	private TestPackage parent;
	private List<IClass> classes = new ArrayList<>();
	private List<IPackage> packages = new ArrayList<>();

	public TestPackage() {
		this(null);
	}

	public TestPackage(TestPackage parent) {
		this.parent = parent;
		if (this.parent != null) {
			this.parent.addPackage(this);
		}
	}

	private void addPackage(TestPackage testPackage) {
		packages.add(testPackage);
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public IPackage getParent() {
		return parent;
	}

	@Override
	public List<IPackage> getPackages() {
		return packages;
	}

	@Override
	public List<IClass> getClasses() {
		return classes;
	}

	@Override
	public String getSimpleName() {
		return null;
	}

	public void addClass(IClass aClass) {
		classes.add(aClass);
	}

}