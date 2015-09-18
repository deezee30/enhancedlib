/*
 * MySQLLib
 *
 * Created on 22 July 2014 at 10:57 AM.
 */

package com.maulss.enhancedlib.database.data;

import com.google.common.annotations.Beta;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

/**
 * Used for generating SQL queries simply by setting specific attributes, types and storage settings.
 *
 * @author  Maulss
 * @see     com.maulss.enhancedlib.database.data.DataType
 * @see     com.maulss.enhancedlib.database.data.StatType
 */
public final class Data implements Cloneable {

	private final String   col;
	private final DataType type;

	private volatile boolean 	nnl = false; 	// NOT NULL
	private volatile boolean 	inc = false; 	// AUTO_INCREMENT
	private volatile boolean 	key = false; 	// KEY
	private volatile boolean 	unq = false; 	// UNIQUE
	private volatile boolean 	uns = false; 	// UNSIGNED
	private volatile String		dft = null;		// DEFAULT X
	private volatile String		xtr = null;		// extra wildcards

	public Data(@NotNull String col,
				@NotNull DataType type) {
		this.col = Validate.notNull(col);
		this.type = Validate.notNull(type);
	}

	/**
	 * Adds a 'NOT NULL' wildcard to the data type.
	 *
	 * @return This instance.
	 */
	public Data notNull() {
		nnl = true;
		return this;
	}

	/**
	 * Adds a 'AUTO_INCREMENT' wildcard to the data type.
	 *
	 * @return This instance.
	 */
	public Data autoIncrement() {
		inc = true;
		return this;
	}

	/**
	 * Adds a 'KEY' wildcard to the data type.
	 *
	 * @return This instance.
	 */
	public Data key() {
		key = true;
		return this;
	}

	/**
	 * Adds a 'UNIQUE' wildcard to the data type.
	 *
	 * @return This instance.
	 */
	public Data unique() {
		unq = true;
		return this;
	}

	/**
	 * Adds an 'UNSIGNED' wildcard to the data type. This can ONLY be used for integers.
	 *
	 * @return This instance;
	 */
	@Beta
	public Data unsigned() {
		uns = true;
		return this;
	}

	/**
	 * Add 'DEFAULT X' wildcard to the data type.
	 *
	 * @param 	string The default value for the column.
	 * @return 	This instance.
	 */
	public Data defaultVal(String string) {
		dft = string;
		return this;
	}

	/**
	 * Adds extra wildcard(s) to the data type.
	 *
	 * @param 	string The extra wildcards for the column.
	 * @return 	This instance.
	 */
	public Data extra(String string) {
		xtr = string;
		return this;
	}

	/**
	 * Compiles the provided wildcards, the data types and other settings and
	 * returns it as a SQL query to be used for inserting columns into tables.
	 *
	 * @return The compiled String that contains the data of the column.
	 */
	public String getDataResult() {
		StringBuilder string = new StringBuilder("`");
		string.append(col);
		string.append("` ");
		string.append(type.getResult());

		if (nnl) string.append(" NOT NULL");
		if (inc) string.append(" AUTO_INCREMENT");
		if (key) string.append(" KEY");
		if (unq) string.append(" UNIQUE");
		if (uns) string.append(" UNSIGNED");
		if (dft != null) string.append(" DEFAULT '").append(dft).append('\'');
		if (xtr != null) string.append(xtr);

		return string.toString();
	}
}