package io.jitclipse.core.launch.config;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface LaunchAttribute {

	String name() default "";

	String defaultValue() default "";

	String namespace() default "";
}
