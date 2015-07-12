/*
 * EnhancedLib
 * 
 * Created on 02 June 2015 at 10:47 PM.
 */

package com.maulss.enhancedlib.net.ftp;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

public final class SimpleFtpClient extends FileTransferClient {

	private static final long serialVersionUID = -8582356528476980465L;

	private final FTPClient client = new FTPClient();

	public SimpleFtpClient(@NotNull String server,
						   @NotNull String user,
						   @NotNull String pass) {
		super(server, user, pass);
	}

	@Override
	public boolean connect() {
		return !isConnected() && performTask(() -> {
			try {
				client.connect(getServer());
				client.login(getUser(), getPass());
				client.enterLocalPassiveMode();
				client.setFileType(FTP.BINARY_FILE_TYPE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public boolean isConnected() {
		return client.isConnected();
	}

	@Override
	public boolean disconnect() {
		return isConnected() && performTask(() -> {
			try {
				client.logout();
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public boolean upload(@NotNull String file,
						  @NotNull String remoteLocation) {
		Validate.notNull(file);
		Validate.notNull(remoteLocation);

		return isConnected() && performTask(() -> {
			try {
				InputStream inputStream = new FileInputStream(file);
				client.storeFile(remoteLocation, inputStream);
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	@Override
	public boolean download(@NotNull String remoteFile,
							@NotNull String location) {
		Validate.notNull(remoteFile);
		Validate.notNull(location);

		return isConnected() && performTask(() -> {
			try {
				client.retrieveFile(remoteFile, new FileOutputStream(location));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	@NotNull
	public FTPClient getClient() {
		return client;
	}
}