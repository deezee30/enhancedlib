/*
 * EnhancedLib
 * 
 * Created on 17 June 2015 at 3:15 PM.
 */

package com.maulss.enhancedlib.service.timer;

import java.util.concurrent.Callable;

public abstract class TimedCallableTask<T> extends TimedTask<T> implements Callable<T> {

	@Override
	public T call() throws Exception {
		return process();
	}
}