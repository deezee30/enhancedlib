/*
 * EnhancedLib
 * 
 * Created on 12 June 2015 at 12:56 AM.
 */

package com.maulss.enhancedlib.loader.plugin;

import com.sun.istack.internal.NotNull;

public class PluginTerminationException extends PluginLoadException {

	private static final long serialVersionUID = -5517204273898049614L;

	public PluginTerminationException(@NotNull String fileName,
									  @NotNull String message) {
		super(fileName, message);
	}

	public PluginTerminationException(@NotNull String fileName,
									  @NotNull String message,
									  @NotNull Throwable cause) {
		super(fileName, message, cause);
	}

	public PluginTerminationException(@NotNull String fileName,
									  @NotNull Throwable cause) {
		super(fileName, cause);
	}

	public PluginTerminationException(@NotNull String fileName,
									  @NotNull String message,
									  @NotNull Throwable cause,
									  boolean enableSuppression,
									  boolean writableStackTrace) {
		super(fileName, message, cause, enableSuppression, writableStackTrace);
	}
}