/*
 * MaulssLib
 * 
 * Created on 25 December 2014 at 8:02 PM.
 */

package com.maulss.enhancedlib.util;

import com.maulss.enhancedlib.EnhancedException;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class FileUtil {

	private final static String UTF8 = "UTF-8";

	/* Disable initialization */
	private FileUtil() {}

	public static byte[] readBytes(File file) throws IOException {
		checkCreate(file);
		int length = (int) file.length();
		byte[] output = new byte[length];
		InputStream in = new FileInputStream(file);
		int offset = 0;
		// normally it should be able to read the entire file with just a single
		// iteration below, but it depends on the whims of the FileInputStream
		while (offset < length) {
			offset += in.read(output, offset, (length - offset));
		}
		in.close();
		return output;
	}

	public static void writeBytes(File file, byte[] bytes) throws IOException {
		checkCreate(file);
		OutputStream out = new FileOutputStream(file);
		out.write(bytes);
		out.close();
	}

	public static void write(File file, String content) throws IOException {
		writeBytes(file, utf8(content));
	}

	public static String read(File file) throws IOException {
		return utf8(readBytes(file));
	}

	public static boolean deleteRecursive(File path) throws FileNotFoundException {
		if (!path.exists()) throw new FileNotFoundException(path.getAbsolutePath());

		boolean ret = true;
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				ret = ret && deleteRecursive(f);
			}
		}
		return ret && path.delete();
	}

	public static byte[] utf8(String string) {
		try {
			return string.getBytes(UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String utf8(byte[] bytes) {
		try {
			return new String(bytes, UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Copy a directory which does not necessarily has to be empty.
	 *
	 * @param   src    Previous directory
	 * @param   target New directory
	 *
	 * @throws  IOException
	 *          If the copy procedure has failed.
	 */
	public static void copy(File src, File target) throws IOException {
		if (src.isDirectory()) {
			if (!target.exists()) target.mkdir();

			for (String child : src.list())
				copy(new File(src, child), new File(target, child));

		} else {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(target);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
			out.close();
			in.close();
		}
	}

	/**
	 * @see java.io.File#createNewFile()
	 */
	private static boolean checkCreate(File file) {
		if (!file.exists()) {
			file.mkdirs();
			try {
				return file.createNewFile();
			} catch (IOException e) {
				try {
					throw new EnhancedException(e);
				} catch (EnhancedException e1) {
					e1.printStackTrace();
				}
			}
		}

		return false;
	}

	public static void unzip(String zipFilePath, String outputDirectory) {
		InputStream 	fileInputStream 		= null;
		InputStream 	bufferedInputStream 	= null;
		ZipInputStream 	zipInputStream 			= null;
		OutputStream 	fileOutputStream 		= null;
		OutputStream 	bufferedOutputStream 	= null;

		try {
			fileInputStream 	= new FileInputStream(zipFilePath);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			zipInputStream 		= new ZipInputStream(bufferedInputStream);
			ZipEntry entry;

			while ((entry = zipInputStream.getNextEntry()) != null) {
				byte[] buffer = new byte[2048];

				fileOutputStream 		= new FileOutputStream(outputDirectory + File.separator + entry.getName());
				bufferedOutputStream 	= new BufferedOutputStream(fileOutputStream, buffer.length);

				int size;
				while ((size = zipInputStream.read(buffer, 0, buffer.length)) != -1) {
					bufferedOutputStream.write(buffer, 0, size);
				}

				bufferedOutputStream.flush();
				bufferedOutputStream.close();
				fileOutputStream.flush();
				fileOutputStream.close();
			}

			zipInputStream.close();
			bufferedInputStream.close();
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			if (fileInputStream != null) try {
				fileInputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (bufferedInputStream != null) try {
				bufferedInputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (zipInputStream != null) try {
				zipInputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (fileOutputStream != null) try {
				fileOutputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (bufferedOutputStream != null) try {
				bufferedOutputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}