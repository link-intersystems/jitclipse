package io.jitclipse.core.launch.config;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import com.link_intersystems.beans.reflect.AbstractBeanProxyHandler;
import com.link_intersystems.eclipse.core.runtime.runtime.IPluginLog;

import io.jitclipse.core.JitCorePlugin;

public class LaunchConfigurationProxySupport<T> extends AbstractBeanProxyHandler<T> {

	private ILaunchConfiguration readableConfig;
	private ILaunchConfigurationWorkingCopy writeableConfig;
	private IPluginLog pluginLog;

	public LaunchConfigurationProxySupport(Class<T> launchAttributeInterface,
			ILaunchConfiguration launchConfiguration) {
		this(launchAttributeInterface, launchConfiguration, JitCorePlugin.getInstance().getPluginLog());
	}

	public LaunchConfigurationProxySupport(Class<T> launchAttributeInterface, ILaunchConfiguration launchConfiguration,
			IPluginLog pluginLog) {
		super(launchAttributeInterface);
		this.readableConfig = launchConfiguration;
		if (readableConfig instanceof ILaunchConfigurationWorkingCopy) {
			writeableConfig = (ILaunchConfigurationWorkingCopy) readableConfig;
		}

		this.pluginLog = pluginLog;
	}

	@Override
	protected void handleSetMethod(Method method, Object[] args, PropertyDescriptor propertyDescriptor) {
		String attributeName = getAttributeName(method, propertyDescriptor);

		if (writeableConfig == null) {
			String msg = MessageFormat.format("Attribute {0} is not writeable.", attributeName);
			throw new UnsupportedOperationException(msg);
		}

		Class<?>[] parameterTypes = method.getParameterTypes();
		Class<?> valueParamType = parameterTypes[0];

		Object argValue = args[0];

		if (String.class.isAssignableFrom(valueParamType)) {
			String value = String.class.cast(argValue);
			writeableConfig.setAttribute(attributeName, value);
		} else {
			String msg = MessageFormat.format("Attribute ''{0}''''s type {1} can not be handled.", attributeName,
					valueParamType.getSimpleName());
			throw new UnsupportedOperationException(msg);
		}
	}

	@Override
	protected Object handleGetMethod(Method method, PropertyDescriptor propertyDescriptor) {
		if ("dirty".equals(propertyDescriptor.getName()) && boolean.class.equals(method.getReturnType())) {
			return isDirty();
		}

		String attributeName = getAttributeName(method, propertyDescriptor);

		try {
			LaunchAttribute launchAttribute = getLaunchAttribute(method);
			String defaultValue = launchAttribute.defaultValue();
			return readableConfig.getAttribute(attributeName, defaultValue);
		} catch (CoreException e) {
			pluginLog.logError(e);
			return null;
		}

	}

	private boolean isDirty() {
		if (writeableConfig != null) {
			return writeableConfig.isDirty();
		}

		return false;
	}

	private String getAttributeName(Method method, PropertyDescriptor propertyDescriptor) {
		StringBuilder attributeNameBuilder = new StringBuilder();

		LaunchAttribute launchAttribute = getLaunchAttribute(propertyDescriptor);

		String namespace = launchAttribute.namespace();
		if (namespace.isBlank()) {
			namespace = getBeanClass().getType().getPackageName();
		}

		attributeNameBuilder.append(namespace);

		String name = launchAttribute.name();

		if (name.isBlank()) {
			name = propertyDescriptor.getName();
		}

		attributeNameBuilder.append(".");
		attributeNameBuilder.append(name);

		return attributeNameBuilder.toString();
	}

	private LaunchAttribute getLaunchAttribute(PropertyDescriptor propertyDescriptor) {
		Method readMethod = propertyDescriptor.getReadMethod();

		LaunchAttribute launchAttribute = getLaunchAttribute(readMethod);

		if (launchAttribute == null) {
			launchAttribute = getLaunchAttribute(propertyDescriptor.getWriteMethod());
		}

		return launchAttribute;
	}

	private LaunchAttribute getLaunchAttribute(Method method) {
		LaunchAttribute launchAttribute = method.getAnnotation(LaunchAttribute.class);
		return launchAttribute;
	}

}
