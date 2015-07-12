/*
 * MaulssLib
 * 
 * Created on 07 February 2015 at 9:47 PM.
 */

package com.maulss.enhancedlib.region;

import com.maulss.enhancedlib.math.MathUtil;
import com.maulss.enhancedlib.math.Vector;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

public class SphericalRegion extends Region {

	private static final long serialVersionUID = -5576209688378810728L;

	private final Vector
			center,
			minBounds,
			maxBounds;
	private final int
			radius,
			volume;

	public SphericalRegion(@NotNull Vector center,
						   int radius) {
		this.center = Validate.notNull(center, "The center point can not be null");
		this.radius = radius = Math.abs(radius);

		volume    = MathUtil.round(4 * Math.PI * Math.pow(radius, 3) / 3);
		minBounds = center.clone().subtract(radius);
		maxBounds = center.clone().add(radius);
	}

	@Override
	public int getVolume() {
		return volume;
	}

	@Override
	public Vector getMinBounds() {
		return minBounds;
	}

	@Override
	public Vector getMaxBounds() {
		return maxBounds;
	}

	@Override
	public boolean contains(Vector vector) {
		return center.distanceSquared(vector) <= Math.pow(radius, 2);
	}

	public Vector getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}
}