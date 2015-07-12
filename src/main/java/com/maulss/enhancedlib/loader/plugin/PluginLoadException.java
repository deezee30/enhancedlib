/*
 * EnhancedLib
 * 
 * Created on 11 June 2015 at 11:31 PM.
 */

package com.maulss.enhancedlib.loader.plugin;

import com.maulss.enhancedlib.loader.JavaFileLoadException;
import com.sun.istack.internal.NotNull;

public class PluginLoadException extends JavaFileLoadException {

	private static final long serialVersionUID = 6660327729345390225L;

	public PluginLoadException(@NotNull String fileName,
							   @NotNull String message) {
		super(fileName, message);
	}

	public PluginLoadException(@NotNull String fileName,
							   @NotNull String message,
							   @NotNull Throwable cause) {
		super(fileName, message, cause);
	}

	public PluginLoadException(@NotNull String fileName,
							   @NotNull Throwable cause) {
		super(fileName, cause);
	}

	public PluginLoadException(@NotNull String fileName,
							   @NotNull String message,
							   @NotNull Throwable cause,
							   boolean enableSuppression,
							   boolean writableStackTrace) {
		super(fileName, message, cause, enableSuppression, writableStackTrace);
	}
}