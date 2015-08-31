/*
 * MySQLLib
 *
 * Created on 22 July 2014 at 12:33 AM.
 */

package com.maulss.enhancedlib.database;

import com.maulss.enhancedlib.database.data.StatType;
import com.maulss.enhancedlib.database.value.Value;
import com.sun.istack.internal.NotNull;

import javax.sql.rowset.CachedRowSet;
import java.io.Serializable;

/**
 * A class used to indentify where a certain row in the database is located.
 * This can be used for anything varying from players to anything else as an entry.
 */
public interface Identity extends Serializable {

	/**
	 * @return The column where the identifier of the player is stored.
	 */
	@NotNull
	StatType getIdentifierColumn();

	/**
	 * @return 	The identifier of the identity.  This can be a name of a player,
	 *         	or anything else that is stored in the {@link #getIdentifierColumn()}.
	 * @see    	#getIdentifierColumn()
	 */
	@NotNull
	String getIdentifier();

	/**
	 * @return	The table that this {@code Identity} is associated with.
	 * @see		Table
	 */
	@NotNull
	Table getTable();

	default boolean containsRow() {
		return getTable().contains(this);
	}

	default void deleteRow() {
		getTable().delete(this);
	}

	default CachedRowSet insertRowToColumns(@NotNull StatType[] columns,
											@NotNull Object... values) {
		return getTable().insertToColumns(columns, values);
	}

	default CachedRowSet insertRow(@NotNull Object... values) {
		return getTable().insert(values);
	}

	default CachedRowSet updateRow(@NotNull StatType column,
								   @NotNull Value value) {
		return getTable().update(this, column, value);
	}

	default CachedRowSet updateRow(@NotNull StatType column,
								   @NotNull Object value) {
		return getTable().update(this, column, value);
	}

	default CachedRowSet updateRow(@NotNull StatType[] columns,
								   @NotNull Value... values) {
		return getTable().update(this, columns, values);
	}

	default CachedRowSet updateRow(@NotNull StatType[] columns,
								   @NotNull Object... values) {
		return getTable().update(this, columns, values);
	}

	default Object getFromRow(@NotNull StatType column) {
		return getTable().get(this, column);
	}

	default Object[] getFromRow(@NotNull StatType... columns) {
		return getTable().get(this, columns);
	}

	default CachedRowSet getRow() {
		return getTable().get(this);
	}
}