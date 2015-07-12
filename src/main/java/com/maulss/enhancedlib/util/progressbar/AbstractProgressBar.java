/*
 * EnhancedLib
 * 
 * Created on 02 June 2015 at 9:43 PM.
 */

package com.maulss.enhancedlib.util.progressbar;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractProgressBar implements ProgressBar {

	private static final long serialVersionUID = 5794255666553984580L;

	private float percentage;
	private int max;

	protected AbstractProgressBar(float percentage, int max) {
		this.percentage = percentage;
		this.max = max;
	}

	@Override
	public ProgressBar setPercent(float percentage) {
		this.percentage = percentage;
		return this;
	}

	@Override
	public float getPercent() {
		return percentage;
	}

	@Override
	public ProgressBar setMax(int max) {
		this.max = max;
		return this;
	}

	@Override
	public int getMax() {
		return max;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("percentage", percentage)
				.append("max", max)
				.toString();
	}
}