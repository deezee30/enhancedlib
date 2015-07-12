/*
 * MySQLLib
 *
 * Created on 22 July 2014 at 10:57 AM.
 */

package com.maulss.enhancedlib.database;

import com.google.common.collect.ImmutableMap;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

import java.util.Map;

/**
 * @author Maulss
 */
public final class Credentials {

	private final String host;
	private final int    port;
	private final String name;
	private final String user;
	private final String pass;

	public Credentials(@NotNull String host,
					   int port,
					   @NotNull String name,
					   @NotNull String user,
					   @NotNull String pass) {
		this.host = Validate.notNull(host);
		this.port = port;
		this.name = Validate.notNull(name);
		this.user = Validate.notNull(user);
		this.pass = Validate.notNull(pass);
	}

	public Credentials(@NotNull Map<String, Object> data) {
		this(
				(String) data.get("host"),
				(int)    data.get("port"),
				(String) data.get("database"),
				(String) data.get("username"),
				(String) data.get("password")
		);
	}

	/**
	 * @return The hostname of the {@link com.maulss.enhancedlib.database.Database} server.
	 */
	@NotNull
	public synchronized String getHost() {
		return host;
	}

	/**
	 * @return The port of the {@link com.maulss.enhancedlib.database.Database} server.
	 */
	@NotNull
	public synchronized int getPort() {
		return port;
	}

	/**
	 * @return The database name of the {@link com.maulss.enhancedlib.database.Database} server.
	 */
	@NotNull
	public synchronized String getName() {
		return name;
	}

	/**
	 * @return The username of the {@link com.maulss.enhancedlib.database.Database} server.
	 */
	@NotNull
	public synchronized String getUser() {
		return user;
	}

	/**
	 * @return The password of the {@link com.maulss.enhancedlib.database.Database} server.
	 */
	@NotNull
	public synchronized String getPass() {
		return pass;
	}

	@NotNull
	public Map<String, Object> serialize() {
		return new ImmutableMap.Builder<String, Object>()
				.put("host", host)
				.put("port", port)
				.put("database", name)
				.put("username", user)
				.put("password", pass)
				.build();
	}
}