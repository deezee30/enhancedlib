/*
 * MySQLLib
 *
 * Created on 22 July 2014 at 12:33 AM.
 */

package com.maulss.enhancedlib.database.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.sun.istack.internal.NotNull;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Used for storing MySQL columns and the settings for the columns.
 * <p>
 * This interface is best used when implemented by an enum, which contains
 * all columns the developer will work with in terms of MySQL.
 * </p>
 *
 * @author  Maulss
 * @see     com.maulss.enhancedlib.database.data.Data
 */
public interface StatType extends Serializable, Cloneable {

	/**
	 * @return The column name inside the table in the form of a String.
	 */
	@NotNull
	String getColumn();

	/**
	 * @return  The {@link com.maulss.enhancedlib.database.data.Data}
	 *          containing the column structure and other information.
	 */
	@NotNull
	Data getData();

	static Data[] data(StatType[] statTypes) {
		int len = statTypes.length;
		Data[] data = new Data[len];
		for (int x = 0; x < len; ++x) {
			data[x] = statTypes[x].getData();
		}

		return data;
	}

	static List<Object> getFromRow(ResultSet result, StatType... columns) {
		List<Object> objs = Lists.newArrayList();
		try {
			for (StatType st : columns) {
				objs.add(result.getObject(st.getColumn()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ImmutableList.copyOf(objs);
	}
}