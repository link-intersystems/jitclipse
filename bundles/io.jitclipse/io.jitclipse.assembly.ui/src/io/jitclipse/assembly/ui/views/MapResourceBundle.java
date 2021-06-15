package io.jitclipse.assembly.ui.views;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class MapResourceBundle extends ResourceBundle {

	private Map<String, Object> properties;

	public MapResourceBundle(Map<String, Object> properties) {
		this.properties = Objects.requireNonNull(properties);
	}

	@Override
	protected Object handleGetObject(String key) {
		return properties.get(key);
	}

	@Override
	public Enumeration<String> getKeys() {
		Iterator<String> iterator = properties.keySet().iterator();
		return new Enumeration<String>() {

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public String nextElement() {
				return iterator.next();
			}
		};
	}

}
