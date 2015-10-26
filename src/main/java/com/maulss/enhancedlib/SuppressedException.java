/*
 * EnhancedLib
 * 
 * Created on 24 September 2015 at 5:49 PM.
 */

package com.maulss.enhancedlib;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class SuppressedException extends EnhancedException {

	private static final long serialVersionUID = -6450641089472167745L;

	public SuppressedException(@NotNull String message,
							   @NotNull Object... components) {
		super(message, components);
	}

	public SuppressedException(@NotNull String message,
							   @NotNull Throwable cause) {
		super(message, cause);
	}

	public SuppressedException(@NotNull Throwable cause) {
		super(cause);
	}

	public SuppressedException(@NotNull String message,
							   Throwable cause,
							   boolean enableSuppression,
							   boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public void printStackTrace() {
		printStackTrace(new PrintStream(new SuppressedOutputStream()));
	}

	private final class SuppressedOutputStream extends OutputStream {

		@Override
		public void write(int b) throws IOException {
			// Does nothing
		}
	}
}