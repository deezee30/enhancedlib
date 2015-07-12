/*
 * MySQLLib
 *
 * Created on 22 July 2014 at 10:57 AM.
 */

package com.maulss.enhancedlib.database;

import com.maulss.enhancedlib.text.Messaging;
import com.maulss.enhancedlib.service.timer.Timer;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.Validate;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

/**
 * @author Maulss
 */
public abstract class Database {

	private static volatile Database database;

	/**
	 * Instantiates the database class via a sub class.  If this
	 * has been previously instantiated already this session, it
	 * closes the previous connection.  Also calls {@link #ping()}.
	 *
	 * @see #ping()
	 */
	{
		if (database != null) Database.closeConnection();
		database = this;
	}


	/**
	 * Checks if the connection is open.  If it's closed, it attempts
	 * to {@link #open()} the connection again.  If an error has been
	 * caught while trying to contact the database, it calls the method
	 * again until 3 attempts have been made.
	 * <p>
	 *  This method must be called whenever a connection to the database
	 * is updated.  For example, querying, updating or obtaining data
	 * to/from the database.
	 * </p>
	 *
	 * @see #open()
	 */
	public final Database ping() throws DatabaseException {
		if (!isOpen()) {
			Timer timer = new Timer().start();
			open();
			Messaging.log(
				"Successfully established a connection with the database server in %sms",
				timer.forceStop().getTime(TimeUnit.MILLISECONDS)
			);
		}

		return this;
	}


	// ---------------------------------------------------------------- //
	//   Subclass Handlers
	// ---------------------------------------------------------------- //


	/**
	 * Opens the connection for the database.  If the database type does
	 * not use {@link java.sql.Connection} as its Connection variable,
	 * it has the ability to return null.  Null may also be returned if
	 * the connection to the database has not been made properly.
	 *
	 * @return 	Null if the database was not found, or the database type
	 * 			does not work with the {@link java.sql.Connection} object.
	 * 			Otherwise it returns the a new connection that has been established.
	 * @throws  com.maulss.enhancedlib.database.DatabaseException
	 * @see     java.sql.Connection
	 */
	@Nullable
	protected abstract Connection open() throws DatabaseException;


	/**
	 * Closes the {@link java.sql.Connection} to the database and returns
	 * the closed connection instance.
	 *
	 * @return  The closed {@link java.sql.Connection} instance.  If the
	 *          database type does not work with the {@link java.sql.Connection}
	 *          object, null can be returned.
	 * @see     java.sql.Connection
	 */
	@Nullable
	protected abstract Connection close() throws DatabaseException;


	/**
	 * Returns the current {@link java.sql.Connection} to the database.
	 *
	 * @return  The {@link java.sql.Connection} instance.  If the database
	 *          type does not work with the {@link java.sql.Connection}
	 *          object, null can be returned.
	 * @see     java.sql.Connection
	 */
	@Nullable
	protected abstract Connection get();


	/**
	 * Checks if the {@link java.sql.Connection} to the database is
	 * currently open and returns the statement.
	 *
	 * @return  True if it the {@link java.sql.Connection} is open.
	 *          False if it is not.
	 * @see     java.sql.Connection
	 */
	protected abstract boolean isOpen() throws DatabaseException;


	/**
	 * Queries the database in a different thread and returns a {@link
	 * javax.sql.rowset.CachedRowSet} with the obtained data from the database.
	 * <p>
	 *  Warning: Use this method for standard queries, because it does not
	 * support the functionality of {@link java.sql.PreparedStatement},
	 * which means this can be used for SQL injection, which is not tolerated.
	 * Either do not use any custom variables in this query, or use {@link
	 * #query(String, Object...)} instead.
	 * </p>
	 *
	 * @param   query
	 * 			The full query that will be executed asynchronously.
	 * @return  The {@link javax.sql.rowset.CachedRowSet} with the obtained
	 *          data from the database.
	 * @see     javax.sql.rowset.CachedRowSet
	 * @see     #query(String, Object...)
	 * @see     java.sql.PreparedStatement
	 */
	@NotNull
	protected abstract CachedRowSet query(@NotNull String query) throws DatabaseException;


	/**
	 * Queries the database in a different thread and returns a {@link
	 * javax.sql.rowset.CachedRowSet} with the obtained data from the database.
	 * <p>
	 *  This method uses {@link java.sql.PreparedStatement}s to execute
	 * secure queries that are not easily able to be injected into.
	 * Simply replace all unknown values as question marks and provide the
	 * values in the method's parameters ({@param values}).
	 * </p>
	 *
	 * @param   query
	 * 			The full query that will be executed asynchronously.
	 * @param   values
	 * 			The values that are used to set indexing in the
	 *          {@link java.sql.PreparedStatement} used to execute
	 *          safe queries to the database.
	 * @return  The {@link javax.sql.rowset.CachedRowSet} with the obtained
	 *          data from the database.
	 * @see     javax.sql.rowset.CachedRowSet
	 * @see     java.sql.PreparedStatement
	 */
	@NotNull
	protected abstract CachedRowSet query(@NotNull String query,
										  @NotNull Object... values) throws DatabaseException;


	/**
	 * Updates the database in a different thread.
	 * <p>
	 *  Warning: Use this method for standard updates, because it does not
	 * support the functionality of {@link java.sql.PreparedStatement},
	 * which means this can be used for SQL injection, which is not tolerated.
	 * Either do not use any custom variables in this update, or use
	 * {@link #update(String, Object...)} instead.
	 * </p>
	 *
	 * @param  	update
	 * 			The full update that will be executed asynchronously.
	 * @return 	A {@code CachedRowSet} containing the auto-generated key(s)
	 * 			generated by the execution of this update.
	 * @see    	#update(String, Object...)
	 * @see    	java.sql.PreparedStatement
	 * @see		Statement#getGeneratedKeys()
	 */
	protected abstract CachedRowSet update(@NotNull String update) throws DatabaseException;


	/**
	 * Updates the database in a different thread.
	 * <p>
	 *  This method uses {@link java.sql.PreparedStatement}s to execute
	 * secure updates that are not easily able to be injected into.  Simply
	 * replace all unknown values as question marks and provide the values
	 * in the method's parameters ({@param values}).
	 * </p>
	 *
	 * @param  	update
	 * 			The full update that will be executed asynchronously.
	 * @param  	values
	 * 			The values that are used to set indexing in the
	 *         	{@link java.sql.PreparedStatement} used to execute
	 *         	safe updates to the database.
	 * @return 	A {@code CachedRowSet} containing the auto-generated key(s)
	 * 			generated by the execution of this update.
	 * @see    	javax.sql.rowset.CachedRowSet
	 * @see    	java.sql.PreparedStatement
	 * @see    	Statement#getGeneratedKeys()
	 */
	protected abstract CachedRowSet update(@NotNull String update,
										   @NotNull Object... values) throws DatabaseException;


	/**
	 * Creates a new database.
	 *
	 * @param 	database
	 * 			The name of the database to create.
	 */
	protected abstract void create(@NotNull String database) throws DatabaseException;


	/**
	 * Returns a new instance of a table.
	 *
	 * @param 	name
	 * 			The name of the table.
	 * @return 	A new instance of the table.
	 * @see    	Table
	 */
	protected abstract Table setupTable(@NotNull String name) throws DatabaseException;


	// ---------------------------------------------------------------- //
	//   Static Methods
	// ---------------------------------------------------------------- //


	/**
	 * Closes the {@link java.sql.Connection} to the database and returns
	 * the closed connection instance.
	 *
	 * @return  The closed {@link java.sql.Connection} instance.  If the
	 *          database type does not work with the {@link
	 *          java.sql.Connection} object, null can be returned.
	 * @see     java.sql.Connection
	 * @see     #close()
	 */
	public static Connection closeConnection() {
		validateExists();

		Messaging.log("Closing Database connection");
		Connection c = null;
		try {
			c = database.close();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		Messaging.debug("Database connection successfully closed");
		return c;
	}


	/**
	 * Returns the current {@link java.sql.Connection} to the database.
	 *
	 * @return  The {@link java.sql.Connection} instance.  If the database
	 *          type does not work with the {@link java.sql.Connection}
	 *          object, null can be returned.
	 * @see     java.sql.Connection
	 * @see     #get()
	 */
	public static Connection getConnection() {
		validateExists();

		return database.get();
	}


	/**
	 * Checks if the {@link java.sql.Connection} to the database is
	 * currently open and returns the statement.
	 *
	 * @return  True if it the {@link java.sql.Connection} is open.
	 *          False if it is not.
	 * @see     java.sql.Connection
	 * @see     #isOpen()
	 */
	public static boolean isConnectionOpen() {
		validateExists();

		try {
			return database.isOpen();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		// Never accessed
		return false;
	}


	/**
	 * Queries the database in a different thread and returns a {@link
	 * javax.sql.rowset.CachedRowSet} with the obtained data from the database.
	 * <p>
	 *  Warning: Use this method for standard queries, because it does
	 * not support the functionality of {@link java.sql.PreparedStatement},
	 * which means this can be used for SQL injection, which is not
	 * tolerated.  Either do not use any custom variables in this query,
	 * or use {@link #sendQuery(String, Object...)} instead.
	 * </p>
	 *
	 * @param   query
	 * 			The full query that will be executed asynchronously.
	 * @return  The {@link javax.sql.rowset.CachedRowSet} with the obtained
	 *          data from the database.
	 * @see     javax.sql.rowset.CachedRowSet
	 * @see     #sendQuery(String, Object...)
	 * @see     java.sql.PreparedStatement
	 */
	@NotNull
	public static CachedRowSet sendQuery(@NotNull String query) {
		validateExists();

		try {
			return database.query(Validate.notNull(query));
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		// Never accessed
		return null;
	}


	/**
	 * Queries the database in a different thread and returns a {@link
	 * javax.sql.rowset.CachedRowSet} with the obtained data from the database.
	 * <p>
	 *  This method uses {@link java.sql.PreparedStatement}s to execute
	 * secure queries that are not easily able to be injected into.
	 * Simply replace all unknown values as question marks and provide
	 * the values in the method's parameters ({@param values}).
	 * </p>
	 *
	 * @param   query
	 * 			The full query that will be executed asynchronously.
	 * @param   values
	 * 			The values that are used to set indexing in the
	 *          {@link java.sql.PreparedStatement} used to execute
	 *          safe queries to the database.
	 * @return  The {@link javax.sql.rowset.CachedRowSet} with the obtained
	 *          data from the database.
	 * @see     javax.sql.rowset.CachedRowSet
	 * @see     java.sql.PreparedStatement
	 * @see     #query(String, Object...)
	 */
	@NotNull
	public static CachedRowSet sendQuery(@NotNull String query,
										 @NotNull Object... values) {
		validateExists();

		try {
			return database.query(Validate.notNull(query), values);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		// Never accessed
		return null;
	}


	/**
	 * Updates the database in a different thread.
	 * <p>
	 *  Warning: Use this method for standard updates, because it does
	 * not support the functionality of {@link java.sql.PreparedStatement},
	 * which means this can be used for SQL injection, which is not tolerated.
	 * Either do not use any custom variables in this update, or use
	 * {@link #sendUpdate(String, Object...)} instead.
	 * </p>
	 *
	 * @param  	update
	 * 			The full update that will be executed asynchronously.
	 * @return 	A {@code CachedRowSet} containing the auto-generated key(s)
	 * 			generated by the execution of this update.
	 * @see    	#sendUpdate(String, Object...)
	 * @see    	java.sql.PreparedStatement
	 * @see     Statement#getGeneratedKeys()
	 */
	public static CachedRowSet sendUpdate(@NotNull String update) {
		validateExists();

		try {
			return database.update(Validate.notNull(update));
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		// Never accessed
		return null;
	}


	/**
	 * Updates the database in a different thread.
	 * <p>
	 *  This method uses {@link java.sql.PreparedStatement}s to execute
	 * secure updates that are not easily able to be injected into.
	 * Simply replace all unknown values as question marks and provide
	 * the values in the method's parameters ({@param values}).
	 * </p>
	 *
	 * @param  	update
	 * 			The full update that will be executed asynchronously.
	 * @param  	values
	 * 			The values that are used to set indexing in the
	 *          {@link java.sql.PreparedStatement} used to execute
	 *          safe updates to the database.
	 * @return 	A {@code CachedRowSet} containing the auto-generated key(s)
	 * 			generated by the execution of this update.
	 * @see    	javax.sql.rowset.CachedRowSet
	 * @see    	java.sql.PreparedStatement
	 * @see     Statement#getGeneratedKeys()
	 * @see    	#query(String, Object...)
	 */
	public static CachedRowSet sendUpdate(@NotNull String update,
										  @NotNull Object... values) {
		validateExists();

		try {
			return database.update(Validate.notNull(update), values);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		// Never accessed
		return null;
	}


	/**
	 * Creates a new database.
	 *
	 * @param  	database
	 * 			The name of the database to create.
	 * @see    	#create(String)
	 */
	public static void createDatabase(@NotNull String database) {
		validateExists();

		try {
			Database.database.create(Validate.notNull(database));
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Returns a new instance of a table.
	 *
	 * @param   table The name of the table
	 *
	 * @return  A new instance of the table.
	 * @see		#setupTable(String)
	 * @see     Table
	 */
	@NotNull
	public static Table getTable(@NotNull String table) {
		try {
			return database.setupTable(Validate.notNull(table));
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		// Never accessed
		return null;
	}


	/**
	 * @return  The type of Database instance that is being used.
	 * @see     com.maulss.enhancedlib.database.Database
	 */
	@Nullable
	protected static Database getDatabase() {
		return database;
	}

	private static void validateExists() {
		if (database == null) {
			try {
				throw new DatabaseException("Database has not been set up yet");
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
	}
}