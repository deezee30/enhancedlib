/*
 * MySQLLib
 *
 * Created on 22 July 2014 at 2:45 AM.
 */

package com.maulss.enhancedlib.database.value;

import com.maulss.enhancedlib.math.MathUtil;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

/**
 * @author  Maulss
 * @see     com.maulss.enhancedlib.database.value.ValueType
 */
public final class Value {

	private final Object value;
	private final ValueType type;

	public Value(@NotNull Object value,
				 @NotNull ValueType type) {
		this.value = Validate.notNull(value);
		this.type = Validate.notNull(type);
	}

	public boolean isInteger() {
		return MathUtil.isInteger(String.valueOf(value));
	}

	@NotNull
	public ValueType getType() {
		return type;
	}

	@NotNull
	public Object getValue() {
		return value;
	}

	public int appendTo(int x) {
		if (!isInteger()) throw new IllegalStateException("Value " + value + " is not an integer");

		return type.equals(ValueType.SET)
				? x
				: type.equals(ValueType.GIVE)
						? x + Integer.valueOf(value.toString())
						: x - Integer.valueOf(value.toString());
	}

	@Override
	public String toString() {
		return value.toString();
	}
}