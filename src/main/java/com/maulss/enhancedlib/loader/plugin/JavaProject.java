/*
 * MaulssLib
 * 
 * Created on 06 February 2015 at 9:29 PM.
 */

package com.maulss.enhancedlib.loader.plugin;

import com.maulss.enhancedlib.collect.EnhancedList;
import com.maulss.enhancedlib.text.Messaging;
import com.maulss.enhancedlib.service.timer.Timer;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class JavaProject implements Serializable {

	private static final long serialVersionUID = -4019878002234130761L;
	private static final PluginManager MANAGER = PluginManager.getInstance();

	private final transient Class<?>[] classes;
	private final transient Timer timer;
	private final Class<?> mainClass;

	private Plugin plugin;

	public JavaProject(@NotNull File file,
					   @NotNull Class<?>[] classes,
					   @NotNull Timer timer,
					   @NotNull Class<?> mainClass) {

		this.classes = Validate.notNull(classes);
		this.timer = Validate.notNull(timer);
		this.mainClass = Validate.notNull(mainClass);

		Messaging.log(
				"Loaded JAR file '%s' with %s classes in %sms",
				file.getPath(),
				classes.length,
				getLoadTime()
		);
	}

	@NotNull
	public Plugin initialize() throws PluginInitializationException {
		if (enabled()) {
			throw new PluginInitializationException(plugin.getName(), "Plugin '" + plugin.getName() + "' is already enabled");
		}

		Timer timer = new Timer().start();

		Object obj;
		try {
			obj = mainClass.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			timer.forceStop();
			throw new PluginInitializationException(plugin.getName(), e);
		}

		if (obj != null) {
			if (Plugin.class.isInstance(obj)) {
				Plugin plugin = (Plugin) obj;

				MANAGER.add(new PluginContainer(this, plugin));
				plugin.onEnable();

				timer.forceStop();

				Messaging.log(
						"Plugin %s version %s%s enabled in %sms",
						plugin.getName(),
						plugin.getVersion(),
						plugin.hasAuthors() ? " " + new EnhancedList<>(plugin.getAuthors()).toReadableList(", ") : "",
						timer.getTime(TimeUnit.MILLISECONDS)
				);
				return plugin;
			} else {
				throw new PluginInitializationException(plugin.getName(), String.format(
						"Plugin instance '%s' was found to not be a sub class of default plugin super class '%s'",
						mainClass.getClass().getName(),
						Plugin.class.getName()
				));
			}
		} else {
			timer.forceStop();
			return null;
		}
	}

	public void terminate() throws PluginTerminationException {
		if (!enabled()) {
			throw new PluginTerminationException(mainClass.getName(), "Plugin '" + mainClass.getName() + "' is not enabled");
		}

		Timer timer = new Timer().start();

		plugin.onDisable();
		MANAGER.remove(new PluginContainer(this, plugin));

		timer.forceStop();

		Messaging.log(
				"Plugin %s disabled in %sms",
				plugin.getName(),
				timer.getTime(TimeUnit.MILLISECONDS)
		);

		plugin = null;
	}

	public boolean enabled() {
		return plugin != null;
	}

	@NotNull
	public Class<?>[] getClasses() {
		return classes;
	}

	@NotNull
	public Class<?> getMainClass() {
		return mainClass;
	}

	@NotNull
	public long getLoadTime() {
		return timer.getTime(TimeUnit.MILLISECONDS);
	}

	public final class PluginContainer {

		private final JavaProject 	project;
		private final Plugin 		plugin;

		public PluginContainer(@NotNull JavaProject project,
							   @NotNull Plugin plugin) {
			this.project = Validate.notNull(project);
			this.plugin = Validate.notNull(plugin);
		}

		@NotNull
		public JavaProject getProject() {
			return project;
		}

		@NotNull
		public Plugin getPlugin() {
			return plugin;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this)
					.append("project", project)
					.append("plugin", plugin)
					.toString();
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof PluginContainer)) return false;

			PluginContainer that = (PluginContainer) o;

			return Objects.equals(project, that.project) &&
					Objects.equals(plugin, that.plugin);
		}

		@Override
		public int hashCode() {
			return Objects.hash(project, plugin);
		}
	}
}