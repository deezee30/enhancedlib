/*
 * EnhancedLib
 * 
 * Created on 11 June 2015 at 10:14 PM.
 */

package com.maulss.enhancedlib.memory;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

import java.io.*;
import java.util.Scanner;

public interface ConfigurationFile {

	String ENCODING = "UTF-8";

	@NotNull
	default Object parseFromResource(@NotNull String path) {
		return parseFromInputStream(getClass().getResourceAsStream(Validate.notNull(path)));
	}

	@NotNull
	default Object parseFromDocument(@NotNull String path) throws FileNotFoundException {
		return parseFromInputStream(new FileInputStream(new File(Validate.notNull(path))));
	}

	@NotNull
	default Object parseFromInputStream(@NotNull InputStream stream) {
		Scanner scanner = new Scanner(Validate.notNull(stream), ENCODING).useDelimiter("\\A");
		String contents = scanner.next();

		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return parseFromContents(contents);
	}

	@NotNull
	Object parseFromContents(@NotNull String contents);
}