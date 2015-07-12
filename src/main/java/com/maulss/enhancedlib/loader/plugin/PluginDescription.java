package com.maulss.enhancedlib.loader.plugin;

import com.sun.istack.internal.NotNull;

import java.lang.annotation.*;

/**
 * @author Maulss
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PluginDescription {

	@NotNull
	String name();

	@NotNull
	String version();

	@NotNull
	String[] authors()
			default {};
}
