/*
 * MySQLLib
 *
 * Created on 22 July 2014 at 10:57 AM.
 */

package com.maulss.enhancedlib.database;

import com.maulss.enhancedlib.EnhancedException;
import com.sun.istack.internal.NotNull;

/**
 * @author Maulss
 */
public class DatabaseException extends EnhancedException {

	private static final long serialVersionUID = 2502593247766479907L;

	public DatabaseException(@NotNull String message) {
		super(message);
	}

	public DatabaseException(@NotNull String message,
							 @NotNull Throwable cause) {
		super(message, cause);
	}

	public DatabaseException(@NotNull Throwable cause) {
		super(cause);
	}

	public DatabaseException(@NotNull String message,
							 @NotNull Throwable cause,
							 boolean enableSuppression,
							 boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}