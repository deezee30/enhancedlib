/*
 * EnhancedLib
 * 
 * Created on 02 June 2015 at 11:09 PM.
 */

package com.maulss.enhancedlib.net.ftp;

import com.maulss.enhancedlib.service.timer.Timer;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public abstract class FileTransferClient implements Serializable, Closeable {

	private static final long serialVersionUID = -1330994605111174606L;

	private final String
			server,
			user,
			pass;

	private final Timer timer = new Timer();

	public FileTransferClient(@NotNull String server,
							  @NotNull String user,
							  @NotNull String pass) {
		this.server = Validate.notNull(server);
		this.user = Validate.notNull(user);
		this.pass = Validate.notNull(pass);
	}

	public abstract boolean connect();

	public abstract boolean isConnected();

	public abstract boolean disconnect();

	public abstract boolean upload(@NotNull String file,
								   @NotNull String remoteLocation);

	public boolean upload(@NotNull String[] files,
						  @NotNull String remoteLocation) {
		Validate.notNull(files);
		Validate.notNull(remoteLocation);

		File dir = new File(remoteLocation);
		if (!dir.exists()) {
			if (!dir.mkdir()) return false;
		}

		boolean ok = true;

		for (String file : files) {
			if (!upload(file, dir + "/" + file)) ok = false;
		}

		return ok;
	}

	public abstract boolean download(@NotNull String remoteFile,
									 @NotNull String location);

	public boolean download(@NotNull String[] remoteFiles,
							@NotNull String location) {
		Validate.notNull(remoteFiles);
		Validate.notNull(location);

		File dir = new File(location);
		if (!dir.exists()) {
			if (!dir.mkdir()) return false;
		}

		boolean ok = true;

		for (String file : remoteFiles) {
			if (!download(file, dir.getPath())) ok = false;
		}

		return ok;
	}

	@NotNull
	public abstract Object getClient();

	public final long getTaskCompletionTimeMillis() {
		return timer.getTime(TimeUnit.MILLISECONDS);
	}

	@Override
	public void close() throws IOException {
		if (!disconnect()) throw new IOException("The stream has already been closed");
	}

	public String getServer() {
		return server;
	}

	protected String getUser() {
		return user;
	}

	protected String getPass() {
		return pass;
	}

	protected final boolean performTask(@NotNull Runnable task) {
		Validate.notNull(task);

		boolean ok = true;

		try {
			task.run();
		} catch (Exception e) {
			ok = false;
		}

		return ok;
	}
}