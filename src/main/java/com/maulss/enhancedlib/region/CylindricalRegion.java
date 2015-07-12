/*
 * MaulssLib
 * 
 * Created on 10 February 2015 at 6:57 PM.
 */

package com.maulss.enhancedlib.region;

import com.maulss.enhancedlib.math.MathUtil;
import com.maulss.enhancedlib.math.Vector;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

public class CylindricalRegion extends Region {

	private static final long serialVersionUID = 2750518492398124058L;

	private final Vector
			base,
			minBounds,
			maxBounds;
	private final int
			radius,
			height,
			volume;

	public CylindricalRegion(@NotNull Vector base,
							 int radius,
							 int height) {
		this.base = Validate.notNull(base, "The base point can not be null");
		this.radius = radius = Math.abs(radius);
		this.height = height = Math.abs(height);

		volume = MathUtil.round(Math.PI * Math.pow(radius, 2) * height);

		minBounds = new Vector(
				base.getX() - radius,
				base.getY(),
				base.getZ() - radius
		);

		maxBounds = new Vector(
				base.getX() + radius,
				base.getY() + height,
				base.getZ() + radius
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
		return Validate.notNull(vector).getY() >= base.getY()
				&& vector.getY() <= base.getY() + height
				&& Math.pow(vector.getX() - base.getX(), 2) + Math.pow(vector.getZ() - base.getZ(), 2) < Math.pow(radius, 2);
	}

	@NotNull
	public Vector getBase() {
		return base;
	}

	public double getRadius() {
		return radius;
	}

	public double getHeight() {
		return height;
	}
}