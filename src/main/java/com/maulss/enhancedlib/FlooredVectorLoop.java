/*
 * MaulssLib
 * 
 * Created on 10 February 2015 at 7:13 PM.
 */

package com.maulss.enhancedlib;

import com.maulss.enhancedlib.math.Vector;

/**
 * Used for looping through a set of three-dimensional points
 * in grid form, usually used in (multi-) dimensional arrays
 * or {@link com.maulss.enhancedlib.region.Region}s containing
 * {@link com.maulss.enhancedlib.math.Vector}s.
 */
@FunctionalInterface
public interface FlooredVectorLoop {

	/**
	 * Loops through a set of three-dimensional (x, y, z) {@link
	 * com.maulss.enhancedlib.math.Vector}s stored inside
	 * (multi-) dimensional arrays or {@link
	 * com.maulss.enhancedlib.region.Region}s and provides an
	 * ability to use each point or {@code Vector} that was found
	 * in the set of {@code Vector}s.
	 *
	 * <p>  For example, the {@link
	 * com.maulss.enhancedlib.region.CuboidRegion} is a sub class of
	 * {@code Region} which holds a fixed solid three-dimensional set
	 * of {@code Vector}s.  This method returns each of those {@code
	 * Vector}s if used correctly.</p>
	 *
	 * <p>  This method was specifically built for accessing stored
	 * points inside a region, such as {@link
	 * com.maulss.enhancedlib.region.Region#loop(FlooredVectorLoop)}
	 * which would work as follows (assuming {@code regionObnect} is
	 * an instance of {@link com.maulss.enhancedlib.region.Region}):
	 * {@code
	 * final List{@literal <}Vector{@literal >} points = new ArrayList<>(regionObject.getVolume());
	 * regionObject.loop(new FlooredVectorLoop() {
	 *
	 *     {@literal @}Override
	 *     public void loop(Vector vector) {
	 *         points.add(vector);
	 *     }
	 * });
	 * }
	 * Or if using {@code Java 1.8}, lamda expressions can simplify this
	 * even more: {@code regionObject.loop(points :: add);}</p>
	 *
	 * @param 	vector
	 * 			The individual point inside a region
	 * @see 	com.maulss.enhancedlib.region.Region#loop(FlooredVectorLoop)
	 * @see		com.maulss.enhancedlib.region.Region#loop(FlooredVectorLoop, boolean)
	 */
	void loop(Vector vector);
}