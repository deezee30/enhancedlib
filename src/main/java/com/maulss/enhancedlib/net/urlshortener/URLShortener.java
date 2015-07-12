/*
 * EnhancedLib
 * 
 * Created on 06 June 2015 at 7:23 PM.
 */

package com.maulss.enhancedlib.net.urlshortener;

import com.google.common.util.concurrent.ListenableFuture;
import com.maulss.enhancedlib.service.ServiceExecutor;
import com.maulss.enhancedlib.service.timer.TimedCallableTask;
import com.maulss.enhancedlib.text.Messaging;
import com.maulss.enhancedlib.text.StringUtil;
import com.sun.istack.internal.NotNull;

import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public abstract class URLShortener extends TimedCallableTask<URL> {

	protected final String longUrl;

	private static final String[] removables = {
			// do not change order
			"ftp://" , "https://" , "http://"
	};

	protected URLShortener(@NotNull String longUrl) {
		for (String keyword : removables) {
			StringUtil.remove(longUrl, keyword);
		}

		this.longUrl = longUrl;
	}

	@NotNull
	public final URL shorten() {
		ListenableFuture<URL> future = ServiceExecutor.getCachedExecutor().submit(this);
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public final String getLongUrl() {
		return longUrl;
	}

	@Override
	public final URL call() throws Exception {
		return executeAndThen(() -> Messaging.debug(
				"URL '%s' was shortened to '%s' in %sms",
				longUrl,
				getT(),
				getTimer().getTime(TimeUnit.MILLISECONDS)
		));
	}

	public static GooGl gooGl(String content) {
		return new GooGl(content);
	}

	public static TinyURL tinyUrl(String content) {
		return new TinyURL(content);
	}
}