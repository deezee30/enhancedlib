/*
 * MaulssLib
 * 
 * Created on 04 March 2015 at 5:50 PM.
 */

package com.maulss.enhancedlib.util.progressbar;

public class CharacterProgressBar extends AbstractProgressBar implements ProgressBar {

	private static final long serialVersionUID = -1224353038689148848L;

	private final char origin;
	private final char replacement;
	private transient String originPrefix = "";
	private transient String replacementPrefix = "";

	public CharacterProgressBar(char origin,
								char replacement,
								float percent) {
		this(origin, replacement, percent, 100);
	}

	public CharacterProgressBar(char origin,
								char replacement,
								float percent,
								int max) {
		super(percent, max);

		this.origin = origin;
		this.replacement = replacement;
	}

	public CharacterProgressBar setOriginPrefix(String originPrefix) {
		this.originPrefix = originPrefix;
		return this;
	}

	public CharacterProgressBar setReplacementPrefix(String replacementPrefix) {
		this.replacementPrefix = replacementPrefix;
		return this;
	}

	public String getProgressBar() {
		StringBuilder sb = new StringBuilder("");
		for (int x = 0; x < getMax(); ++x) {
			sb.append(x < (getPercent() * getMax())
							? replacementPrefix + replacement
							: originPrefix + origin
			);
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		return getProgressBar();
	}
}