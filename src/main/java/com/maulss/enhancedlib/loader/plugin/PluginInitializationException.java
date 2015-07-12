/*
 * EnhancedLib
 * 
 * Created on 11 June 2015 at 11:26 PM.
 */

package com.maulss.enhancedlib.loader.plugin;

import com.sun.istack.internal.NotNull;

public class PluginInitializationException extends PluginLoadException {

	private static final long serialVersionUID = -4164599405823811942L;

	public PluginInitializationException(@NotNull String fileName,
										 @NotNull String message) {
		super(fileName, message);
	}

	public PluginInitializationException(@NotNull String fileName,
										 @NotNull String message,
										 @NotNull Throwable cause) {
		super(fileName, message, cause);
	}

	public PluginInitializationException(@NotNull String fileName,
										 @NotNull Throwable cause) {
		super(fileName, cause);
	}

	public PluginInitializationException(@NotNull String fileName,
										 @NotNull String message,
										 @NotNull Throwable cause,
										 boolean enableSuppression,
										 boolean writableStackTrace) {
		super(fileName, message, cause, enableSuppression, writableStackTrace);
	}
}