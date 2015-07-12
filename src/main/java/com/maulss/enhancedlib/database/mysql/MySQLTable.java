/*
 * MySQLLib
 *
 * Created on 22 July 2014 at 2:39 AM.
 */

package com.maulss.enhancedlib.database.mysql;

import com.maulss.enhancedlib.collect.EnhancedList;
import com.maulss.enhancedlib.database.Database;
import com.maulss.enhancedlib.database.DatabaseException;
import com.maulss.enhancedlib.database.Identity;
import com.maulss.enhancedlib.database.Table;
import com.maulss.enhancedlib.database.data.Data;
import com.maulss.enhancedlib.database.data.StatType;
import com.maulss.enhancedlib.database.value.Value;
import com.maulss.enhancedlib.database.value.ValueType;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

import javax.sql.rowset.CachedRowSet;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.stream.Collectors;

public final class MySQLTable extends Table {

	private static final long serialVersionUID = -2544088579505931851L;

	MySQLTable(@NotNull String table) {
		super(table.toLowerCase(Locale.ENGLISH));
	}

	@Override
	public void create(@NotNull Data[] types) {
		Validate.notNull(types, "Could not create a new table because the data types are null");

		StringBuilder colBuilder = new StringBuilder();

		for (int x = 0; x < types.length; ++x) {
			colBuilder.append(types[x].getDataResult());

			if (types.length - 1 != x) colBuilder.append(", ");
		}

		Database.sendUpdate(String.format("CREATE TABLE IF NOT EXISTS `%s` (%s);", this, colBuilder));
	}

	@Override
	public void create(@NotNull Data[] types,
					   @NotNull StatType index) {
		if (index == null) {
			create(types);
			return;
		}

		Validate.notNull(types, "Could not create a new table because the data types are null");

		StringBuilder colBuilder = new StringBuilder();

		for (int x = 0; x < types.length; ++x) {
			colBuilder.append(types[x].getDataResult()).append(", ");

			// Append INDEX at the very end
			if (types.length - 1 == x) {
				colBuilder.append("INDEX (");
				colBuilder.append(index.getColumn());
				colBuilder.append(")");
			}
		}

		Database.sendUpdate(String.format("CREATE TABLE IF NOT EXISTS `%s` (%s);", this, colBuilder));
	}

	@Override
	public boolean exists() {
		boolean exists = false;

		try {
			DatabaseMetaData meta = Database.getConnection().getMetaData();
			// Obtain all tables that match the criteria -- Should only be 1
			ResultSet res = meta.getTables(null, null, toString(), new String[] {"TABLE"});
			exists = res.next();
			res.close();
		} catch (SQLException e) {
			try {
				throw new DatabaseException(e);
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
		}

		return exists;
	}

	@Override
	public CachedRowSet insertToColumns(@NotNull StatType[] columns,
										@NotNull Object... values) {
		Validate.notNull(columns, "Could not insert values into columns because the columns are null");
		Validate.notNull(values, "Could not insert values into columns because the values are null");
		Validate.notEmpty(values, "Could not insert values into columns because the values array is empty");

		if (columns.length != values.length) {
			try {
				throw new DatabaseException(
						"The amount of columns in the database does not match the amount of values that were put in"
				);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			return null;
		}

		StringBuilder colBuilder = new StringBuilder();
		StringBuilder valBuilder = new StringBuilder();

		for (int x = 0; x < columns.length; ++x) {
			colBuilder.append("`").append(columns[x].getColumn()).append("`");
			valBuilder.append("?");

			if (columns.length - 1 != x) {
				colBuilder.append(", ");
				valBuilder.append(", ");
			}
		}

		return Database.sendUpdate(String.format("INSERT INTO `%s` (%s) VALUES (%s);", this, colBuilder, valBuilder), values);
	}

	@Override
	public CachedRowSet insert(@NotNull Object... values) {
		Validate.notNull(values, "Could not insert values into columns because the values are null");
		Validate.notEmpty(values, "Could not insert values into columns because the size of them is 0");

		StringBuilder valBuilder = new StringBuilder();

		for (int x = 0; x < values.length; ++x) {
			valBuilder.append("?");

			if (values.length - 1 != x) {
				valBuilder.append(", ");
			}
		}

		return Database.sendUpdate(String.format("INSERT INTO `%s` VALUES (%s);", this, valBuilder), values);
	}

	@Override
	public void delete(@NotNull Identity identity) {
		Validate.notNull(identity, "Could not delete the identity because it is null");

		Database.sendUpdate(String.format(
				"DELETE FROM `%s` WHERE `%s` = ?;",
				this,
				identity.getIdentifierColumn().getColumn()
		), identity.getIdentifier());
	}

	@Override
	public boolean contains(@NotNull Identity identity) {
		if (identity == null) return false;

		try {
			return get(identity).next();
		} catch (SQLException e) {
			try {
				throw new DatabaseException(e);
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}

	@Override
	public CachedRowSet update(@NotNull Identity identity,
							   @NotNull StatType[] columns,
							   @NotNull Value... values) {
		Validate.notNull(identity, "The identity must not be null in order to update its data");
		Validate.notEmpty(columns, "The type of column to update is null");
		Validate.notEmpty(values, "The value to update for this identity is null");
		int len = columns.length;
		Validate.isTrue(len == values.length, "The amount of columns must equal to the amount of values provided");

		String[] strVals = new String[len + 1];
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < len; ++x) {
			sb.append("`");
			sb.append(columns[x].getColumn());
			sb.append("` = ");

			Value value = values[x];
			strVals[x] = value.toString();
			// I'm not even going to bother explaining what is going on here.
			sb.append(value.isInteger() && !value.getType().equals(ValueType.SET)
					? String.format(
							Locale.ENGLISH,
							"`%s` %s ?",
							columns[x].getColumn(),
							value.getType().equals(ValueType.GIVE) ? "+" : "-")
					: "?"
			);

			if (x != len - 1) {
				sb.append(", ");
			}
		}

		strVals[len] = identity.getIdentifier();

		return Database.sendUpdate(String.format(
				"UPDATE `%s` SET %s WHERE `%s` = ?;",
				this,
				sb,
				identity.getIdentifierColumn().getColumn()
		), strVals);
	}

	@Override
	@NotNull
	public Object[] get(@NotNull Identity identity,
						@NotNull StatType... columns) {
		Validate.notNull(identity, "The identity must not be null in order to obtain its data");
		Validate.notNull(columns, "The type of column to obtain is null");
		Validate.noNullElements(columns, "None of the columns can be null");

		int l = columns.length;
		Object[] values = new Object[l];

		@SuppressWarnings("serial")
		CachedRowSet row = l == 0 ? get(identity) : Database.sendQuery(String.format(
				"SELECT %s FROM `%s` WHERE `%s` = ? LIMIT 1;",
				new EnhancedList<String>(l) {{
					addAll(new EnhancedList<>(columns)
									.stream()
									.map(column -> String.format("`%s`", column.getColumn()))
									.collect(Collectors.toList())
					);
					// In the form of "x, y, z" as opposed to "[x,y,z]"
				}}.toReadableList(", "),
				this,
				identity.getIdentifierColumn().getColumn()
		), identity.getIdentifier());

		try {
			if (row != null && row.next()) {
				int x = 0;
				// Increment x first of all then use it
				while (x < l) values[++x - 1] = row.getObject(x);
			}
		} catch (SQLException e) {
			try {
				throw new DatabaseException(e);
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
		}

		return values;
	}

	@Override
	@NotNull
	public CachedRowSet get(@NotNull Identity identity) {
		Validate.notNull(identity, "The identity must not be null in order to obtain its data");

		return Database.sendQuery(String.format(
				"SELECT * FROM `%s` WHERE `%s` = ? LIMIT 1;",
				this,
				identity.getIdentifierColumn().getColumn()
		), identity.getIdentifier());
	}

	@Override
	@NotNull
	public CachedRowSet getAll() {
		return Database.sendQuery("SELECT * FROM `" + this + "`;");
	}

	@Override
	public int getTotalRows() {
		int total = 0;

		CachedRowSet row = Database.sendQuery(String.format("SELECT count(*) FROM `%s`;", this));

		try {
			// noinspection ConstantConditions
			if (row.next()) total = row.getInt(1);
		} catch (SQLException e) {
			try {
				throw new DatabaseException(e);
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
		}

		return total;
	}
}