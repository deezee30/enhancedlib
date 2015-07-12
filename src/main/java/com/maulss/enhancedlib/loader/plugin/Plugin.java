/*
 * MaulssLib
 * 
 * Created on 06 February 2015 at 8:44 PM.
 */

package com.maulss.enhancedlib.loader.plugin;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class Plugin {

	private static final Pattern VALID_NAME = Pattern.compile("[a-zA-Z]+");

	private String name;
	private String version;
	private Collection<String> authors;

	public Plugin() {
		try {
			if (!getClass().isAnnotationPresent(PluginDescription.class)) {
				throw new PluginInitializationException(
						name,
						"Main class must have " + PluginDescription.class.getName() + " annotation"
				);
			}

			PluginDescription pd = getClass().getAnnotation(PluginDescription.class);
			name = pd.name();
			version = pd.version();
			authors = Arrays.asList(pd.authors());

			if (!VALID_NAME.matcher(name).matches()) {
				throw new PluginInitializationException(name, "Invalid plugin name: " + name);
			}
		} catch (PluginInitializationException e) {
			e.printStackTrace();
		}
	}

	public abstract void onEnable();

	public abstract void onDisable();

	public final String getName() {
		return name;
	}

	public final String getVersion() {
		return version;
	}

	public final Collection<String> getAuthors() {
		return authors;
	}

	public final boolean hasAuthors() {
		return getAuthors().size() > 0;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", name)
				.append("version", version)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Plugin)) return false;

		Plugin plugin = (Plugin) o;
		return Objects.equals(name, plugin.name) &&
				Objects.equals(version, plugin.version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, version);
	}
}