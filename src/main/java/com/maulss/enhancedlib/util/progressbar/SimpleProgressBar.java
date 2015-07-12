/*
 * EnhancedLib
 * 
 * Created on 02 June 2015 at 10:13 PM.
 */

package com.maulss.enhancedlib.util.progressbar;

import javax.swing.*;

public class SimpleProgressBar extends AbstractProgressBar implements ProgressBar {

	private static final long serialVersionUID = 3843834105107353733L;

	private final JProgressBar bar = new JProgressBar();

	protected SimpleProgressBar(float percentage, int max) {
		super(percentage, max);
	}

	@Override
	public ProgressBar setPercent(float percentage) {
		bar.setValue((int) (percentage * 100));
		return super.setPercent(percentage);
	}

	public JProgressBar getDelegate() {
		return bar;
	}
}