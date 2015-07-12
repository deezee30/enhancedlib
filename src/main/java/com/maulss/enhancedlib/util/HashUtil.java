/*
 * MaulssLib
 * 
 * Created on 25 December 2014 at 6:15 PM.
 */

package com.maulss.enhancedlib.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtil {

	private static final String        DEFAULT_ENCODING = "UTF-8";
	private static final BASE64Encoder ENCODER          = new BASE64Encoder();
	private static final BASE64Decoder DECODER          = new BASE64Decoder();

	/* Disable initialization */
	private HashUtil() {}

	public static String toBase64(Object obj) {
		try {
			return ENCODER.encode(obj.toString().getBytes(DEFAULT_ENCODING));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static String fromBase64(Object obj) {
		try {
			return new String(DECODER.decodeBuffer(obj.toString()), DEFAULT_ENCODING);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * The fastest way possible to hash a {@param a} with a MD5 algorithm. Usually takes about 0 - 1 milliseconds.
	 * The hashed String has a length of 32 characters with no dashes.
	 *
	 * @param a The String to be serialized.
	 *
	 * @return The hashed String in length of 32 characters, with no dashes.
	 */
	public static String hash(String a) {
		try {

			MessageDigest b = MessageDigest.getInstance("MD5");
			byte[] c = b.digest(a.getBytes());
			StringBuilder d = new StringBuilder();

			for (byte e : c)
				d.append(Integer.toHexString((e & 0xFF) | 0x100).substring(1, 3));

			return d.toString();
		} catch (NoSuchAlgorithmException ignored) {}
		return "";
	}
}