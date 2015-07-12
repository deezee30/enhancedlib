/*
 * MySQLLib
 *
 * Created on 22 July 2014 at 10:57 AM.
 */

package com.maulss.enhancedlib.database.data;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * @author Maulss
 */
public enum DataType {

	// =: Numerical
	// Type     | Column Name     | Attr 1 | Attr 2 |
	TINYINT		("TINYINT(?)", 		127),
	SMALLINT	("SMALLINT(?)", 	255),
	MEDIUMINT	("MEDIUMINT(?)", 	255),
	INT			("INT(?)", 			255),
	BIGINT		("BIGINT(?)", 		255),
	FLOAT		("FLOAT(?,?)", 		16, 		6),
	DOUBLE		("DOUBLE(?,?)", 	16, 		6),

	// =: Text
	// Type     | Column Name     | Attr 1 |
	CHAR		("CHAR(?)", 		255),
	VARCHAR		("VARCHAR(?)", 		255),
	TEXT		("TEXT", 			255),
	MEDIUMTEXT	("MEDIUMTEXT", 		255),
	LONGTEXT	("LONGTEXT", 		255),

	// =: Dates
	// Type     | Column Name     | Attr 1 |
	TIMESTAMP	("TIMESTAMP", 		0),
	TIME		("TIME", 			0),
	DATE		("DATE", 			0),

	// =: Misc
	// Type     | Column Name     | Attr 1 | Attr 2 |
	CUSTOM		("?", 				0),
	CUSTOM_1	("?(?)", 			0),
	CUSTOM_2	("?(?,?)", 			0, 			0);

	private String   type;
	private Object[] attr;

	DataType(String type, Object... attr) {
		this.type = type;
		this.attr = attr;
	}

	/**
	 * Provides optional attributes if the {@link com.maulss.enhancedlib.database.data.DataType} contains specific variables
	 * that can be used alongside them.  For example, {@link #VARCHAR} can hold an attribute
	 * specifying the maximum capacity for that specific column.
	 * <ping>
	 * If the attribute(s) has/have not been set, the default options will be used (Not recommended).
	 * </ping>
	 *
	 * @param   attr The attributes to set
	 *
	 * @return  This instance.
	 */
	public DataType setAttributes(@NotNull Object... attr) {
		Validate.notEmpty(attr);
		Validate.noNullElements(attr);

		this.attr = attr;
		return this;
	}

	/**
	 * Used for custom {@code DataType}s. By default the {@link #CUSTOM} enum entries are unknown.
	 * However, {@link #custom(String, Object...)} calls this method to set a proper title
	 * (and attributes) for custom {@code DataType}s.
	 *
	 * @param   type The data name of the custom {@link com.maulss.enhancedlib.database.data.DataType}
	 *
	 * @return  This instance.
	 * @see     #custom(String, Object...)
	 */
	private DataType modifyData(@NotNull String type) {
		this.type = Validate.notEmpty(type);
		return this;
	}

	/**
	 * Compiles the provided DataType with its optional attributes and returns it as part of a
	 * query for creating columns in tables in SQL databases.
	 *
	 * @return A part of a SQL query.
	 */
	public String getResult() {
		for (int x = 0; x < StringUtils.countMatches(type, "?"); ++x) {
			type = type.replaceAll("\\?", attr[x].toString());
		}

		return type;
	}

	/**
	 * Generates custom {@link com.maulss.enhancedlib.database.data.DataType}s that could be used for types of data that are not present
	 * in the list of enum entries in this enum.
	 *
	 * @param   type The custom name of the data type
	 * @param   attr The custom attributes for the custom data type (if any are needed).
	 *               The length of the Object array defines which of the custom enums to
	 *               use depending on the attributes needed
	 *
	 * @return  The instance of the custom data type.
	 * @see     #CUSTOM
	 * @see     #CUSTOM_1
	 * @see     #CUSTOM_2
	 */
	public static DataType custom(@NotNull String type,
								  @NotNull Object... attr) {
		int attrs = attr.length;
		return attrs == 0
				? CUSTOM.modifyData(type).setAttributes(attr)
				: attrs == 1
						? CUSTOM_1.modifyData(type).setAttributes(attr)
						: CUSTOM_2.modifyData(type).setAttributes(attr);
	}
}