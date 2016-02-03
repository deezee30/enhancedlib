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

	private final String url;
	private final String name;
	private final String user;
	private final String pass;

	public Credentials(@NotNull String url,
					   @NotNull String name,
					   @NotNull String user,
					   @NotNull String pass) {
		this.url  = Validate.notNull(url);
		this.name = Validate.notNull(name);
		this.user = Validate.notNull(user);
		this.pass = Validate.notNull(pass);
	}

	public Credentials(@NotNull Map<String, Object> data) {
		this(
				(String) data.get("url"),
				(String) data.get("database"),
				(String) data.get("username"),
				(String) data.get("password")
		);
	}

	/**
	 * @return	The URL including the hostname and port (and perhaps properties) of the
	 * 			{@link com.maulss.enhancedlib.database.Database} server.
	 */
	@NotNull
	public synchronized String getUrl() {
		return url;
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
				.put("url",			url)
				.put("database",	name)
				.put("username",	user)
				.put("password",	pass)
				.build();
	}
}