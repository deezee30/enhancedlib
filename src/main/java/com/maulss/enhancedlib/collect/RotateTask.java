/*
 * EnhancedLib
 * 
 * Created on 16 June 2015 at 2:21 AM.
 */

package com.maulss.enhancedlib.collect;

@FunctionalInterface
public interface RotateTask<E> {

	void onRotate(E element);
}