package io.jitclipse.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

import com.link_intersystems.eclipse.core.runtime.runtime.IPluginLog;

public class ImageRegisitryConfigurer {

	private ImageRegistry imageRegistry;
	private IPluginLog pluginLog;

	public ImageRegisitryConfigurer(IPluginLog pluginLog, ImageRegistry imageRegistry) {
		this.pluginLog = pluginLog;
		this.imageRegistry = imageRegistry;

	}

	public void register(String key, String pathOrUrl) {
		if (pathOrUrl.startsWith("platform:/plugin/")) {
			try {
				URL url = new URL(pathOrUrl);
				ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
				register(key, imageDescriptor);
			} catch (MalformedURLException e) {
				String msg = MessageFormat.format("Unable to register {0} as {1}", pathOrUrl, key);
				pluginLog.logError(msg, e);
			}
		}

	}

	public void register(String key, ImageDescriptor imageDescriptor) {
		imageRegistry.put(key, imageDescriptor);
	}
}
