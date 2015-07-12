/*
 * MaulssLib
 * 
 * Created on 07 February 2015 at 8:24 PM.
 */

package com.maulss.enhancedlib.region;

import com.maulss.enhancedlib.math.MathUtil;
import com.maulss.enhancedlib.math.Vector;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

public class CuboidRegion extends Region {

	private static final long serialVersionUID = -1670751903460496963L;

	private final Vector
			min,
			max;
	private final int
			volume,
			width,
			height,
			depth;

	public CuboidRegion(@NotNull Vector min,
						@NotNull Vector max) {
		this.min = Validate.notNull(min, "The min point can not be null");
		this.max = Validate.notNull(max, "The max point can not be null");

		width  = MathUtil.floor(max.getX() - min.getX() + 1);
		height = MathUtil.floor(max.getY() - min.getY() + 1);
		depth  = MathUtil.floor(max.getZ() - min.getZ() + 1);

		volume = width * height * depth;
	}

	@Override
	public int getVolume() {
		return volume;
	}

	@NotNull
	@Override
	public Vector getMinBounds() {
		return min;
	}

	@NotNull
	@Override
	public Vector getMaxBounds() {
		return max;
	}

	@Override
	public boolean contains(@NotNull Vector vector) {
		return Validate.notNull(vector).getX() >= min.getX()
				&& vector.getX() <= max.getX()
				&& vector.getY() >= min.getY()
				&& vector.getY() <= max.getY()
				&& vector.getZ() >= min.getZ()
				&& vector.getZ() <= max.getZ();
	}

	/**
	 * @return width of the cuboid (x-direction)
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return height of the cuboid (y-direction)
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return depth of the cuboid (z-direction)
	 */
	public int getDepth() {
		return depth;
	}
}