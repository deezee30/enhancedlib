/*
 * MaulssLib
 * 
 * Created on 07 February 2015 at 8:21 PM.
 */

package com.maulss.enhancedlib.region;

import com.google.common.annotations.Beta;
import com.maulss.enhancedlib.FlooredVectorLoop;
import com.maulss.enhancedlib.collect.EnhancedList;
import com.maulss.enhancedlib.math.Vector;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Used as a three-dimensional set of three-dimensional {@link Vector}s
 * that provides essential functions for working with points inside the
 * region.
 *
 * <p>Since {@link Vector} consists of (x, y, z) {@code double} values,
 * it can contain any real number, meaning the depth of the region can be
 * infinite, except it's not possible to do that virtually.  This doesn't
 * necessarily mean the points in the region cannot be whole numbers.
 *  However all recorded points that can be found in this region instance
 * will always be {@code int}s, since it's impossible to work with {@code
 * double}s like that.  All points in the region dynamically become floored
 * using {@link Vector#getFloorX()}, {@link Vector#getFloorY()} and {@link
 * Vector#getFloorZ()}.</p>
 *
 * <p>Of course, an implementation of this three-dimensional region can
 * make use of its functions while still working with a two-dimensional
 * region, simply by keeping all {@code y} coordinates the same.</p>
 *
 * @author  Maulss
 * @since 	JDK 1.8
 * @see 	Cloneable
 * @see 	Iterable<Vector>
 * @see		Vector
 * @see		FlooredVectorLoop
 */
public abstract class Region implements Cloneable, Serializable, Iterable<Vector> {

	private static final long serialVersionUID = 3272199880401532227L;

	private final transient int minX = Math.min(
			getMinBounds().getFloorX(),
			getMaxBounds().getFloorX()
	);

	private final transient int minY = Math.min(
			getMinBounds().getFloorY(),
			getMaxBounds().getFloorY()
	);

	private final transient int minZ = Math.min(
			getMinBounds().getFloorZ(),
			getMaxBounds().getFloorZ()
	);

	// Loop through every point and add it to list
	@SuppressWarnings("serial")
	private final EnhancedList<Vector> points = new EnhancedList<Vector>(getVolume()) {
		{ loop(points::add); }
	};

	/**
	 * @return The volume of the region in {@code int} units squared form.
	 */
	@NotNull
	public abstract int getVolume();

	/**
	 * @return  The lowest corner of how the region was rendered, no matter
	 * 			if it's inside or outside the actual (non) polygonal region.
	 */
	@NotNull
	public abstract Vector getMinBounds();

	/**
	 * @return 	The highest corner of how the region was rendered, no matter
	 * 			if it's inside or outside the actual (non) polygonal region.
	 */
	@NotNull
	public abstract Vector getMaxBounds();

	/**
	 * Checks if a {@link Vector} point is actually located inside the region
	 * (set of {@code Vector}s).
	 *
	 * <p><b>NB:</b> The {@code Vector} should be checked to be inside the
	 * polygonal or non-plogynal region itself, instead of checking the bounds.
	 *  That means that if the region was a {@code SphericalRegion} object,
	 * then the check would involve checking if the point is inside the
	 * sphere, instead of the bounds encasing it in the form of a cuboid.</p>
	 *
	 * @param 	vector
	 * 			The vector to check whether it's inside the region
	 * @return 	True if the point is inside of the region.  False if otherwise
	 */
	public abstract boolean contains(@NotNull Vector vector);

	/**
	 * Checks if a different {@link Region} object contains within this region
	 * by making sure all points in {@param region} are also points of the region
	 * in {@code this} instance.
	 *
	 * <p>For {@param region} to be a sub region of {@code this} region, the
	 * {@link #getVolume()} of {@code this} region must definitely be bigger or
	 * equal to {@param region}'s {@link Region#getVolume()}</p>
	 *
	 * @param   region
	 * 			The region to check whether it intersects this super region
	 * @return  True if {@param region} is inside {@code this} region.  False if otherwise
	 * @see		#contains(Vector)
	 */
	public final synchronized boolean contains(Region region) {
		if (region == null || getVolume() < region.getVolume()) {
			return false;
		}

		for (Vector point : points)
			if (!region.contains(point))
				return false;

		return true;
	}

	/**
	 * Checks if a different {@link Region} object intersects or collides with this
	 * region by making a check to see if at least a single point in {@param region}
	 * has the same coordinates as {@code this} region.
	 *
	 * @param   region
	 * 			The region to check whether it intersects this region
	 * @return  True if {@param region} collides with {@code this} region.  False if otherwise
	 * @see     #contains(Vector)
	 */
	public final synchronized boolean intersects(Region region) {
		if (region == null) {
			return false;
		}

		for (Vector point : points)
			if (region.contains(point))
				return true;

		return false;
	}

	/**
	 * @return  The average center point in this region.  Obtained by checking
	 * 			the {@link #getMinBounds()} and {@link #getMaxBounds()}
	 * @see 	Vector#get3DCentroid(Vector...)
	 */
	@NotNull
	public final synchronized Vector getCentroid() {
		return Vector.get3DCentroid(getMinBounds(), getMaxBounds());
	}

	/**
	 * @return	A completely random point within the region.  The value
	 * 			returned here must strictly return {@code true} if used in
	 * 			{@link #contains(Vector)}
	 * @see		EnhancedList#getRandomElement()
	 */
	@NotNull
	public final Vector getRandomPoint() {
		return points.getRandomElement();
	}

	/**
	 * Loops through every point ({@link Vector}) in this region and returns
	 * them for use.
	 *
	 * <p>  This method was specifically built for accessing stored
	 * points inside a region which would work as follows:
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
	 * @param   loop
	 * 			The runnable that runs code for each vector returned
	 * @see		FlooredVectorLoop
	 * @see     #loopBounds(FlooredVectorLoop)
	 */
	public final void loop(@NotNull FlooredVectorLoop loop) {
		loop(loop, false);
	}

	/**
	 * Loops through every point ({@link Vector}) in this region's <b>bounds</b>
	 * and returns them for use.
	 *
	 * <p>  This method was specifically built for accessing stored
	 * points inside a region which would work as follows:
	 * {@code
	 * final List{@literal <}Vector{@literal >} points = new ArrayList<>(regionObject.getVolume());
	 * regionObject.loopBounds(new FlooredVectorLoop() {
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
	 * @param   loop
	 * 			The runnable that runs code for each vector returned
	 * @see     FlooredVectorLoop
	 * @see     #loop(FlooredVectorLoop)
	 */
	public final void loopBounds(@NotNull FlooredVectorLoop loop) {
		loop(loop, true);
	}

	private synchronized void loop(FlooredVectorLoop loop,
								   boolean boundsIncluded) {
		Validate.notNull(loop);

		for (int x = minX; x <= minY; ++x) {
			for (int y = minZ; y <= minX; ++y) {
				for (int z = minY; z <= minZ; ++z) {
					Vector loopingVector = new Vector(x, y, z);
					if (!boundsIncluded || !contains(loopingVector)) {
						continue;
					}

					loop.loop(loopingVector);
				}
			}
		}
	}

	/**
	 * Joins {@code this} region with {@param other} region provided they suffice
	 * the requirements to be joined:
	 * <ul>
	 *     <li>Both regions must be intersecting each other, not just touching.</li>
	 *     <li>One region can not be inside the other.</li>
	 *     <li>If one region {@link #equals(Object)} the other, first region is returned.</li>
	 * </ul>
	 *
	 * <p>The returned region will have a volume of {@code v1 + v2 - v3} - {@code v1}
	 * being the volume of region 1, {@code v2} being the volume of region 2 and
	 * {@code v3} being the volume of the intersecting part of both regions.</p>
	 *
	 * <p>The returned region will have its minimum bounds set to the minimum
	 * value taken from both bounds using {@link Vector#getMinimum(Vector, Vector)}
	 * and will have its maximum bounds set to the maximum value taken from both
	 * bounds using {@link Vector#getMaximum(Vector, Vector)}.</p>
	 *
	 * @param 	other
	 * 			The other region to join {@code this} region with.
	 * @return	The new colliding {@link Region} object that contains the points
	 * 			of both regions.  If both regions equal to each other, {@code this}
	 * 			instance is returned.
	 * @throws 	RegionBoundsException
	 *			If either {@code this} region or {@param other} is inside one
	 *			another.  This is checked by {@link #contains(Region)}.
	 * @throws  RegionBoundsException
	 * 			If the regions are separated, ie; both regions have to intersect
	 * 			(collide) with each other in order to be joined.  Otherwise these
	 * 			two regions are two separate regions and should be kept that way.
	 * @see		#getMinBounds()
	 * @see		#getMaxBounds()
	 */
	@Beta
	@NotNull
	public final Region joinWith(Region other) throws RegionBoundsException {
		if (equals(other)) return this;

		if (contains(other) || other.contains(this)) {
			throw new RegionBoundsException("Regions can not be inside one another");
		}

		if (!intersects(other)) {
			throw new RegionBoundsException("One region must intersect the other in order to join them");
		}

		return new Region() {

			private static final long serialVersionUID = -6718823314869950117L;

			private final int
					volume;
			private final Vector
					minBounds,
					maxBounds;

			{
				// Final int array instead of final int in order to modify it anonymously
				final int[] volume = {0};

				// Anonymous modification
				loop(vector -> ++volume[0]);
				this.volume = volume[0];

				minBounds = Vector.getMinimum(
						Region.this.getMinBounds(),
						other.getMinBounds()
				);

				maxBounds = Vector.getMaximum(
						Region.this.getMaxBounds(),
						other.getMaxBounds()
				);
			}

			@Override
			public int getVolume() {
				return volume;
			}

			@NotNull
			@Override
			public Vector getMinBounds() {
				return minBounds;
			}

			@NotNull
			@Override
			public Vector getMaxBounds() {
				return maxBounds;
			}

			@Override
			public boolean contains(@NotNull Vector vector) {
				return Region.this.contains(Validate.notNull(vector)) || other.contains(vector);
			}
		};
	}

	@Override
	public Iterator<Vector> iterator() {
		return points.iterator();
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null
				&& obj instanceof Region
				&& getVolume() == ((Region) obj).getVolume()
				&& getMinBounds().equals(((Region) obj).getMinBounds())
				&& getMaxBounds().equals(((Region) obj).getMaxBounds())
				&& points.equals(((Region) obj).points);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(getClass().getSimpleName())
				.append("minBounds", getMinBounds())
				.append("maxBounds", getMaxBounds())
				.append("volume", getVolume())
				.append("points", points)
				.build();
	}
}