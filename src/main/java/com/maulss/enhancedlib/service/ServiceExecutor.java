/*
 * EnhancedLib
 * 
 * Created on 06 June 2015 at 7:55 PM.
 */

package com.maulss.enhancedlib.service;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.maulss.enhancedlib.net.http.Form;
import com.sun.istack.internal.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public final class ServiceExecutor {

	// Used by pasters and url shorteners
	// Will never be shut down as it will be reused
	private static final ListeningExecutorService executor = newAsyncExecutor();

	private ServiceExecutor() {}

	public static ListeningExecutorService getCachedExecutor() {
		return executor;
	}

	@NotNull
	public static Form mozillaForm() {
		return Form.create().add("User-Agent", "Mozilla/5.0");
	}

	public static ListeningExecutorService newAsyncExecutor() {
		return MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
	}

	public static ListeningExecutorService newAsyncExecutor(ThreadFactory tf) {
		return MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1, tf));
	}

	public static ListeningExecutorService newAsyncExecutor(String name) {
		return newAsyncExecutor(new ThreadFactoryBuilder().setNameFormat(name + "-%d").build());
	}
}