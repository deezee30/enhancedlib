/*
 * EnhancedLib
 * 
 * Created on 03 June 2015 at 12:43 AM.
 */

package com.maulss.enhancedlib.net.ftp;

import com.jcraft.jsch.*;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.Validate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public final class SimpleSftpClient extends FileTransferClient{

	private static final long serialVersionUID = 5519935597945964030L;

	private Session session;
	private ChannelSftp sftp;

	public SimpleSftpClient(@NotNull String server,
							@NotNull String user,
							@NotNull String pass) {
		super(server, user, pass);

		try {
			session = new JSch().getSession(user, server, 22);
		} catch (JSchException e) {
			e.printStackTrace();
			return;
		}

		session.setPassword(pass);
		session.setConfig("StrictHostKeyChecking", "no");
	}

	@Override
	public boolean connect() {
		return !isConnected() && performTask(() -> {
			try {
				session.connect();

				sftp = (ChannelSftp) session.openChannel("sftp");
				sftp.setInputStream(System.in);
				sftp.setOutputStream(System.out);
				sftp.connect();
			} catch (JSchException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public boolean isConnected() {
		return session.isConnected() && sftp.isConnected();
	}

	@Override
	public boolean disconnect() {
		return isConnected() || performTask(() -> {
			try {
				if (sftp != null) sftp.exit();
				if (session != null) session.disconnect();
			} catch (Throwable t) {
				t.printStackTrace();
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
				sftp.put(new FileInputStream(file), remoteLocation);
			} catch (SftpException | FileNotFoundException e) {
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
				sftp.get(remoteFile, location);
			} catch (SftpException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	@NotNull
	public ChannelSftp getClient() {
		return sftp;
	}
}