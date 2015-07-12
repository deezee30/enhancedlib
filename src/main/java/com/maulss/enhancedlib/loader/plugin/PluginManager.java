/*
 * EnhancedLib
 * 
 * Created on 11 June 2015 at 11:20 PM.
 */

package com.maulss.enhancedlib.loader.plugin;

import com.google.common.base.Optional;
import com.maulss.enhancedlib.Wrapper;
import com.maulss.enhancedlib.collect.EnhancedList;
import com.maulss.enhancedlib.loader.JavaFileLoadException;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

public final class PluginManager {

	private static final PluginManager INSTANCE = new PluginManager();

	private final EnhancedList<JavaProject.PluginContainer> cache = new EnhancedList<>();

	// Disable public initialization
	// Hence singleton
	private PluginManager() {}

	JavaProject.PluginContainer add(@NotNull JavaProject.PluginContainer container) {
		Validate.notNull(container);
		if (!isPluginEnabled(container.getPlugin().getName())) cache.add(container);

		return container;
	}

	JavaProject.PluginContainer remove(@NotNull JavaProject.PluginContainer container) {
		Validate.notNull(container);
		if (isPluginEnabled(container.getPlugin().getName())) cache.remove(container);

		return container;
	}

	public Optional<Plugin> getPlugin(@NotNull String name) {
		Validate.notNull(name);

		for (JavaProject.PluginContainer container : cache) {
			Plugin plugin = container.getPlugin();
			if (plugin.getName().equalsIgnoreCase(name)) {
				return Optional.of(plugin);
			}
		}

		return Optional.absent();
	}

	JavaProject.PluginContainer getPluginContainer(@NotNull Plugin plugin) {
		Validate.notNull(plugin);

		Wrapper<JavaProject.PluginContainer> container = new Wrapper<>();
		cache.stream().filter(c -> c.getPlugin().equals(plugin)).forEach(container :: set);

		if (container.isPresent()) return container.get().get();

		try {
			throw new JavaFileLoadException(
					plugin.getName(),
					"Plugin appeared to be enabled but ended up without a PluginContainer"
			);
		} catch (JavaFileLoadException e) {
			e.printStackTrace();
		}

		// Never called
		return null;
	}

	public boolean isPluginEnabled(@NotNull String name) {
		return getPlugin(Validate.notEmpty(Validate.notNull(name))).isPresent();
	}

	@NotNull
	public static PluginManager getInstance() {
		return INSTANCE;
	}
}