/*
 * EnhancedLib
 * 
 * Created on 12 June 2015 at 4:19 AM.
 */

package com.maulss.enhancedlib.memory;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class YamlConfigurationFile implements ConfigurationFile {

	private final Yaml yaml = new Yaml(new DumperOptions() {{
		// options
	}});

	@Override
	public Object parseFromInputStream(@NotNull InputStream stream) {
		Validate.notNull(stream);

		return yaml.load(stream);
	}

	public Iterable<Object> parseAllFromInputStream(@NotNull InputStream stream) {
		Validate.notNull(stream);

		return yaml.loadAll(stream);
	}

	@Override
	public Object parseFromContents(@NotNull String contents) {
		Validate.notNull(contents);

		return yaml.load(contents);
	}

	public Iterable<Object> parseAllFromContents(@NotNull String contents) {
		Validate.notNull(contents);

		return yaml.loadAll(contents);
	}
}