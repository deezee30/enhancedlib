/*
 * MySQLLib
 *
 * Created on 22 July 2014 at 1:45 PM.
 */

package com.maulss.enhancedlib.database.mysql;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.maulss.enhancedlib.database.*;
import com.maulss.enhancedlib.service.ServiceExecutor;
import com.maulss.enhancedlib.service.timer.Timer;
import com.maulss.enhancedlib.text.Messaging;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.rowset.CachedRowSetImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.*;

/**
 * @author 	Maulss
 * @see 	com.maulss.enhancedlib.database.Database
 * @see    	Table
 * @see 	MySQLTable
 * @see 	MySQLProperties
 * @since 	{@code JDK 1.8}
 */
public class MySQL extends Database {

	private final MySQLProperties properties;
	private volatile Timer timer = new Timer();
	private ListeningExecutorService executor;
	private Connection connection;

	public MySQL(@NotNull MySQLProperties properties) throws DatabaseException {
		this.properties = Validate.notNull(properties, "Database credentials must not be null");
		ping();
	}

	@Override
	@Nullable
	protected Connection open() throws DatabaseException {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			final Credentials c = properties.getCredentials();
			connection = DriverManager.getConnection(c.getUrl(), c.getUser(), c.getPass());

			// Create such a thread pool that creates new threads as needed, but
			// will reuse previously constructed threads when they are available.
			// This thread pool will drastically increase performance for these
			// small asynchronous tasks as they will be reused.
			executor = ServiceExecutor.newAsyncExecutor("MySQL Pool");
		} catch (SQLException | ClassNotFoundException e) {
			throw new DatabaseException("An error occurred while attempting to establish a database connection", e);
		}

		return connection;
	}

	@Override
	@Nullable
	protected final Connection close() throws DatabaseException {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				executor.shutdown();
			} else {
				Messaging.log("Attempted to close the connection to the database but it is already closed");
			}
		} catch (SQLException e) {
			throw new DatabaseException("An error occurred while attempting to close a database connection", e);
		}

		return connection;
	}

	@Override
	@Nullable
	protected final Connection get() {
		return connection;
	}

	@Override
	protected final boolean isOpen() throws DatabaseException {
		if (connection == null) return false;

		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	@NotNull
	protected final CachedRowSet query(@NotNull String query) throws DatabaseException {
		checkExecution(query);

		Future<CachedRowSet> future = executor.submit(() -> {

			// Record heavy tasks with a timer as usual
			timer.start();

			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			// Using CachedRowSet so that we can manipulate the ResultSet
			// later on after it was closed.
			CachedRowSet row = new CachedRowSetImpl();
			row.populate(result);

			timer.forceStop();

			return row;
		});

		CachedRowSet row;

		try {
			row = future.get();
		} catch (InterruptedException | ExecutionException e) {
			timer.forceStop();
			throw new DatabaseException(
					"An error occurred while attempting to query the database: \"" + query + "\"", e);
		}

		return row;
	}

	@Override
	@NotNull
	protected final CachedRowSet query(@NotNull String query,
									   @NotNull Object... values) throws DatabaseException {
		checkExecution(query, values);

		Future<CachedRowSet> future = executor.submit(() -> {

			// Record heavy tasks with a timer as usual
			timer.start();

			PreparedStatement statement = connection.prepareStatement(query);
			set(statement, values);
			ResultSet result = statement.executeQuery();
			// Using CachedRowSet so that we can manipulate the ResultSet
			// later on after it was closed.
			CachedRowSet row = new CachedRowSetImpl();
			row.populate(result);

			result.close();
			statement.close();

			timer.forceStop();

			return row;
		});

		CachedRowSet row;

		try {
			row = future.get();
		} catch (InterruptedException | ExecutionException e) {
			timer.forceStop();
			throw new DatabaseException("An error occurred while attempting to query the database: \""
					+ query + "\" -- with values: " + Arrays.toString(values), e);
		}

		return row;
	}

	@Override
	@NotNull
	protected final CachedRowSet update(@NotNull String update) throws DatabaseException {
		checkExecution(update);

		Future<CachedRowSet> future = executor.submit(() -> {

			// Record heavy tasks with a timer as usual
			timer.start();

			// Using CachedRowSet so that we can manipulate the ResultSet
			// later on after it was closed.
			CachedRowSet row = new CachedRowSetImpl();

			// Make sure the execution returns the auto-generated keys
			try (PreparedStatement statement = connection.prepareStatement(update, Statement.RETURN_GENERATED_KEYS)) {
				statement.executeUpdate();

				row.populate(statement.getGeneratedKeys());
			} catch (SQLException e) {
				Messaging.debug("An error occurred while attempting to update the database: \"%s\" -- %s",
						update, e);
			}

			timer.forceStop();

			return row;
		});

		CachedRowSet row = null;

		try {
			row = future.get();
		} catch (InterruptedException | ExecutionException e) {
			timer.forceStop();
			Messaging.debug(
					"An error occurred while attempting to retrieve generated keys while updating the database: \"%s\" -- %s",
					update, e);
		}

		return row;
	}

	@Override
	@NotNull
	protected final CachedRowSet update(@NotNull String update,
										@NotNull Object... values) throws DatabaseException {
		checkExecution(update, values);

		Future<CachedRowSet> future = executor.submit(() -> {

			// Record heavy tasks with a timer as usual
			timer.start();

			// Using CachedRowSet so that we can manipulate the ResultSet
			// later on after it was closed.
			CachedRowSet row = new CachedRowSetImpl();

			// Make sure the execution returns the auto-generated keys
			try (PreparedStatement statement = connection.prepareStatement(update, Statement.RETURN_GENERATED_KEYS)) {
				set(statement, values);
				statement.executeUpdate();

				row.populate(statement.getGeneratedKeys());
			} catch (SQLException e) {
				Messaging.debug("An error occurred while attempting to update the database: \"%s\" -- with values: %s -- %s",
						update, Arrays.toString(values), e);
			} finally {
				timer.forceStop();
			}

			return row;
		});

		CachedRowSet row = null;

		try {
			row = future.get();
		} catch (InterruptedException | ExecutionException e) {
			timer.forceStop();
			Messaging.debug(
					"An error occurred while attempting to retrieve generated keys while updating the database: \"%s\" -- %s",
					update, e);
		}

		return row;
	}

	@Override
	protected final void create(@NotNull String database) throws DatabaseException {
		update(String.format("CREATE DATABASE IF NOT EXISTS `%s`;", database.toLowerCase(Locale.ENGLISH)));
	}

	@Override
	protected Table setupTable(@NotNull String name) throws DatabaseException {
		return new MySQLTable(name);
	}

	private void set(@NotNull PreparedStatement statement,
					 @NotNull Object... values) {
		Validate.notNull(statement);

		int x = 0;
		while (x < values.length) {
			Object value = values[x];
			if (value == null) {
				try {
					// Replace null values with an empty String
					statement.setString(++x, "");
				} catch (SQLException e) {
					try {
						throw new DatabaseException(e);
					} catch (DatabaseException e1) {
						e1.printStackTrace();
					}
				}

				continue;
			}

			try {
				// If value turned out to not be null, essentially set it as an Object.
				// Here we also check if the value matches for a UUID, whether it contains
				// dashes or not.  If the value was found to match for a UUID, remove all
				// dashes to make it of length 32 to save space in the database and decrease
				// some errors and bugs the dashes can potentially cause.
				statement.setObject(++x, UUIDUtil.checkForValidUuid(value));
			} catch (SQLException e) {
				try {
					throw new DatabaseException(e);
				} catch (DatabaseException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private boolean checkExecution(@NotNull String execution,
								   @NotNull Object... vars) throws DatabaseException {
		// Make sure Database != null and is also connected
		Validate.notNull(Database.getDatabase()).ping();
		if (executor.isShutdown()) executor = ServiceExecutor.newAsyncExecutor();

		Throwable error = null;

		if (Strings.isNullOrEmpty(execution)) {
			error = new IllegalArgumentException("Syntax invalid for execution");
		}

		if (vars.length > 0 && vars.length != StringUtils.countMatches(execution, "?")) {
			error = new IllegalArgumentException("Column count doesn't match value count");
		}

		if (error == null) {
			// No error has occurred -- String looks ok so far
			return true;
		}

		throw new DatabaseException(error);
	}
}