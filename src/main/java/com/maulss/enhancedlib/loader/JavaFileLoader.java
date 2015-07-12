/*
 * MaulssLib
 * 
 * Created on 06 February 2015 at 8:42 PM.
 */

package com.maulss.enhancedlib.loader;

import com.google.common.annotations.Beta;
import com.google.common.collect.Lists;
import com.maulss.enhancedlib.collect.EnhancedList;
import com.maulss.enhancedlib.loader.plugin.JavaProject;
import com.maulss.enhancedlib.service.timer.Timer;
import com.maulss.enhancedlib.text.Messaging;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class JavaFileLoader {

	// Filter JAR files by ".jar" extensions
	private static final FilenameFilter JAR_FILENAME_FILTER = (dir, name) -> name.endsWith(".jar");

	private final File file;
	private final String packaging;
	private final Class<?> main;

	public JavaFileLoader(@NotNull File file,
						  @NotNull String packaging,
						  @Nullable Class<?> main) {
		Validate.notNull(file, "File/directory cannot be null");
		Validate.notNull(packaging, "Packaging for JAR must not be null");

		this.file = file;
		this.packaging = packaging;
		this.main = main;
	}

	public JavaFileLoader(@NotNull File file,
						  @NotNull String packaging) {
		this(file, packaging, null);
	}

	@Beta
	@NotNull
	public Collection<JavaProject> load() throws JavaFileLoadException {
		if (file.exists()) {
			if (file.isDirectory()) {
				Messaging.debug(Messaging.BORDER);
				Messaging.debug("Starting to load JAR files from directory '%s'", file.getPath());
				Messaging.debug(Messaging.BORDER);

				// Begin counting the total time taken for every plugin to load in milliseconds
				long millis = 0L;

				// Apply filter and list files
				File[] files = file.listFiles(JavaFileLoader.JAR_FILENAME_FILTER);
				int len = files.length;
				Collection<JavaProject> jars = new ArrayList<>(len);

				for (File file1 : files) {
					JavaProject jar = load(file1);
					jars.add(jar);

					// Dirty workaround: In case load fails, check for null timings
					millis += jar != null ? jar.getLoadTime() : 0L;
				}

				Messaging.debug("Loaded %s jar files in %sms", len, millis);
				Messaging.debug(Messaging.BORDER);

				return jars;
			} else {
				return Lists.newArrayList(load(file));
			}
		} else {
			Messaging.log(
					"Directory '%s' does not exist -- creating directory; no JAR files were loaded",
					file.getPath()
			);

			// noinspection ResultOfMethodCallIgnored
			file.mkdir();
			return Lists.newArrayList();
		}
	}

	@Beta
	@NotNull
	public JavaProject load(@NotNull File file) throws JavaFileLoadException {
		Validate.notNull(file);

		Messaging.log(
				"Loading JAR file '%s', main class being '%s'",
				file.getPath(),
				main.getName()
		);

		Timer timer = new Timer().start();

		// All classes that will be loaded in the project
		EnhancedList<Class<?>> classes = new EnhancedList<>();

		Class<?> mainClass = null;

		try (JarFile jarFile = new JarFile(file.getPath())) {
			Enumeration e = jarFile.entries();

			URL[] urls;
			try {
				urls = new URL[] {new URL("jar:file:" + file.getPath() + "!/")};
			} catch (MalformedURLException e1) {
				throw new JavaFileLoadException(file.getPath(), e1);
			}

			URLClassLoader cl = URLClassLoader.newInstance(urls);

			while (e.hasMoreElements()) {
				JarEntry je = (JarEntry) e.nextElement();

				if (je.isDirectory() || !je.getName().endsWith(".class")) {
					continue;
				}

				String className = je.getName().substring(0, je.getName().length() - 6).replace('/', '.');

				if (!className.startsWith(packaging)) {
					continue;
				}

				Class<?> clazz;

				try {
					clazz = cl.loadClass(className);
					Messaging.debug("Static initialization was made for class " + className);
				} catch (ClassNotFoundException e1) {
					Messaging.log(
							"Skipping '%s', super-class ClassLoader was not able to make a static initialization for class '%s': %s",
							file.getPath(),
							className,
							e1
					);

					return null;
				}

				classes.add(clazz);

				if (clazz.isInstance(main)) {
					if (!Messaging.logIf(
							mainClass != null,
							"Another main class ('%s') has been found in JAR '%s' - this one will be used instead",
							className,
							file.getPath()
					)) {
						Messaging.log("Main class '%s' has been found in JAR '%s'", className, file.getPath());
					}

					mainClass = clazz;
				}
			}
		} catch (IOException e) {
			throw new JavaFileLoadException(file.getPath(), e);
		} finally {
			timer.forceStop();
		}

		return new JavaProject(file, classes.toArray(new Class<?>[classes.size()]), timer, mainClass);
	}
}