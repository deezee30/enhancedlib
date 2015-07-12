/*
 * EnhancedLib
 * 
 * Created on 12 June 2015 at 1:43 PM.
 */

package com.maulss.enhancedlib.loader;

import com.maulss.enhancedlib.EnhancedException;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

public class JavaFileLoadException extends EnhancedException {

	private static final long serialVersionUID = 3133381191666013944L;

	protected final String fileName;

	public JavaFileLoadException(@NotNull String fileName,
								 @NotNull String message) {
		super("Plugin loader could not load JAR file '" + Validate.notNull(fileName) + "': " + Validate.notNull(message));
		this.fileName = fileName;
	}

	public JavaFileLoadException(@NotNull String fileName,
								 @NotNull String message,
								 @NotNull Throwable cause) {
		super(
				"Plugin loader could not load JAR file '" + Validate.notNull(fileName) + "': " + Validate.notNull(message),
				Validate.notNull(cause)
		);
		this.fileName = fileName;
	}

	public JavaFileLoadException(@NotNull String fileName,
								 @NotNull Throwable cause) {
		this(
				fileName,
				"",
				cause
		);
	}

	public JavaFileLoadException(@NotNull String fileName,
								 @NotNull String message,
								 @NotNull Throwable cause,
								 boolean enableSuppression,
								 boolean writableStackTrace) {
		super(
				"Plugin loader could not load JAR file '" + Validate.notNull(fileName) + "': " + Validate.notNull(message),
				Validate.notNull(cause),
				enableSuppression,
				writableStackTrace
		);
		this.fileName = fileName;
	}
}