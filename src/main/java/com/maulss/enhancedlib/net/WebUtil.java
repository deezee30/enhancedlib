/*
 * MaulssLib
 * 
 * Created on 04 March 2015 at 9:04 PM.
 */

package com.maulss.enhancedlib.net;

import com.maulss.enhancedlib.EnhancedException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class WebUtil {

	/* Disable initialization */
	private WebUtil() {}

	private static final ExecutorService executor = Executors.newSingleThreadExecutor();

	public static void ping(String url) {
		try {
			ping(new URL(url));
		} catch (MalformedURLException e) {
			try {
				throw new EnhancedException(e);
			} catch (EnhancedException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void ping(final URL url) {
		executor.execute(() -> {
			try {
				url.openStream().close();
			} catch (IOException e) {
				try {
					throw new EnhancedException(e);
				} catch (EnhancedException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}