/*
 * EnhancedLib
 * 
 * Created on 12 June 2015 at 2:39 PM.
 */

package com.maulss.enhancedlib;

import com.google.common.base.Optional;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Objects;

public class Wrapper<T> implements Serializable {

	private static final long serialVersionUID = -2841564336534030880L;

	private T field;

	public Wrapper() {}

	public Wrapper(T field) {
		this.field = field;
	}

	public boolean isPresent() {
		return get().isPresent();
	}

	public Optional<T> get() {
		return Optional.fromNullable(field);
	}

	public T set(T field) {
		return this.field = field;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("field", get().get())
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Wrapper && Objects.equals(toString(), o.toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(get().get());
	}
}