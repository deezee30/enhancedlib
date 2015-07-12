/*
 * EnhancedLib
 * 
 * Created on 23 June 2015 at 3:00 AM.
 */

package com.maulss.enhancedlib.net.paster;

import com.maulss.enhancedlib.EnhancedException;
import com.sun.istack.internal.NotNull;

public class PasteException extends EnhancedException {

	private static final long serialVersionUID = 8188274555648338076L;

	public PasteException(@NotNull String message) {
		super(message);
	}

	public PasteException(@NotNull String message,
						  @NotNull Throwable cause) {
		super(message, cause);
	}

	public PasteException(@NotNull Throwable cause) {
		super(cause);
	}

	public PasteException(@NotNull String message,
						  @NotNull Throwable cause,
						  boolean enableSuppression,
						  boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}