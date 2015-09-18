/*
 * MaulssLib
 * 
 * Created on 27 February 2015 at 11:42 PM.
 */

package com.maulss.enhancedlib;

import com.google.common.base.Optional;
import com.maulss.enhancedlib.text.Messaging;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

import java.net.URL;

public class EnhancedException extends Exception {

	private static final long serialVersionUID = -6450641089472167745L;

	private final Optional<URL> pasteLink = Optional.absent();

	public EnhancedException(@NotNull String message,
							 @NotNull Object... components) {
		super(Messaging.buildMessage(Validate.notNull(message), components));
	}

	public EnhancedException(@NotNull String message,
							 @NotNull Throwable cause) {
		super(Validate.notNull(message), Validate.notNull(cause));
	}

	public EnhancedException(@NotNull Throwable cause) {
		super(Validate.notNull(cause));
	}

	public EnhancedException(@NotNull String message,
							 @NotNull Throwable cause,
							 boolean enableSuppression,
							 boolean writableStackTrace) {
		super(
				Validate.notNull(message),
				Validate.notNull(cause),
				enableSuppression,
				writableStackTrace
		);
	}

	/*
	@Override
	public void printStackTrace() {
		Console console = System.console();
		if (console == null) {
			if (getCause() == null) super.printStackTrace();
			else 					getCause().printStackTrace();
			return;
		}

		String stacktrace = ExceptionUtils.getStackTrace(this);

		try {
			synchronized (this) {
				ListenableFuture<URL> pasteUrl = PASTER.paste(stacktrace);
				pasteLink = Optional.of(pasteUrl.get());

				StringBuilder msg = new StringBuilder();
				for (String part : splitIfTooLong(toString(), 54)) {
					msg.append("\n\t\t   ");
					msg.append(part);
				}

				Messaging.log(
						"%s%s%s:\n\t\t   %s",
						Messaging.getNoPrefixChar(),
						Messaging.BORDER,
						msg.toString(),
						pasteLink.get()
				);
			}
		} catch (InterruptedException | ExecutionException e) {
			Messaging.log(
					"%s%s: %s",
					Messaging.getNoPrefixChar(),
					e,
					e.getMessage()
			);
			Thread.currentThread().interrupt();
		} finally {
			Messaging.log(Messaging.getNoPrefixChar() + Messaging.BORDER);
		}
	}
	*/

	public Optional<URL> getPasteLink() {
		return pasteLink;
	}

	private String[] splitIfTooLong(String origin, int sectionLength) {
		int length = origin.length();
		String prototype = origin;
		String[] parts = new String[(length / sectionLength) + 1];
		int x = 0;
		while (prototype.length() > sectionLength) {
			parts[x] = prototype.substring(0, sectionLength);
			prototype = prototype.substring(sectionLength);
			++x;
		}

		int remainingChars = length % sectionLength;
		parts[parts.length - 1] = origin.substring(length - remainingChars);

		return parts;
	}
}