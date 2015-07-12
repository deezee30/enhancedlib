/*
 * EnhancedLib
 * 
 * Created on 13 June 2015 at 4:10 PM.
 */

package com.maulss.enhancedlib.service.timer;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.Validate;

public abstract class TimedTask<T> {

	private final Timer timer = new Timer();
	private T t;

	@Nullable
	public final T execute() throws Exception {
		return executeAndThen(() -> {});
	}

	@Nullable
	public final T executeAndThen(@NotNull Runnable runnable) throws Exception {
		timer.onFinishExecute(Validate.notNull(runnable)).start();
		t = process();
		timer.forceStop();
		return t;
	}

	@NotNull
	public final Timer getTimer() {
		return timer;
	}

	@Nullable
	public final T getT() {
		return t;
	}

	@Nullable
	protected abstract T process() throws Exception;
}