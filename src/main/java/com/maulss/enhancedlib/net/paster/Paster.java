/*
 * MaulssLib
 * 
 * Created on 08 February 2015 at 2:40 PM.
 */

package com.maulss.enhancedlib.net.paster;

import com.google.common.util.concurrent.ListenableFuture;
import com.maulss.enhancedlib.service.ServiceExecutor;
import com.maulss.enhancedlib.service.timer.TimedCallableTask;
import com.maulss.enhancedlib.text.Messaging;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public abstract class Paster extends TimedCallableTask<URL> {

	private final String content;

	protected Paster(@NotNull String content) {
		this.content = Validate.notNull(content);
	}

	@NotNull
	public final URL paste() throws PasteException {
		ListenableFuture<URL> future = ServiceExecutor.getCachedExecutor().submit(this);
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new PasteException(e);
		}
	}

	@Override
	public final URL call() throws Exception {
		return executeAndThen(() -> Messaging.debug(
				"Content pasted in %sms: %s",
				getTimer().getTime(TimeUnit.MILLISECONDS),
				getT()
		));
	}

	@NotNull
	protected final String getContent() {
		return content;
	}

	public static Gist gist(@NotNull Map<String, String> files) {
		return new Gist(files);
	}

	public static Hastebin hastebin(String content) {
		return new Hastebin(content);
	}

	public static Pastebin pastebin(String content) {
		return new Pastebin(content);
	}
}