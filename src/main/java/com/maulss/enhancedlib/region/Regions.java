/*
 * MaulssLib
 * 
 * Created on 10 February 2015 at 4:22 PM.
 */

package com.maulss.enhancedlib.region;

import com.maulss.enhancedlib.collect.EnhancedList;
import com.maulss.enhancedlib.math.Vector;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

import java.util.List;

public final class Regions {

	private Regions() {}

	/**
	 * Gets all regions that contain the given point.
	 *
	 * @param   point
	 * 			Point to check against
	 * @return  Regions which contain this point
	 * @see 	Region#contains(Vector)
	 */
	@NotNull
	public static synchronized List<Region> getContaining(@NotNull Vector point,
														  @NotNull Region... regions) {
		Validate.notNull(point);
		Validate.notNull(regions);
		Validate.notEmpty(regions);
		Validate.noNullElements(regions);

		EnhancedList<Region> contain = new EnhancedList<>(regions.length);

		new EnhancedList<>(regions)
				.stream()
				.filter(region -> region.contains(point))
				.forEach(contain :: add);

		return contain;
	}
}