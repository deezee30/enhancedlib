/*
 * EnhancedLib
 * 
 * Created on 02 June 2015 at 9:39 PM.
 */

package com.maulss.enhancedlib.util.progressbar;

import java.io.Serializable;

public interface ProgressBar extends Serializable {

	ProgressBar setPercent(float percentage);

	float getPercent();

	ProgressBar setMax(int max);

	int getMax();
}