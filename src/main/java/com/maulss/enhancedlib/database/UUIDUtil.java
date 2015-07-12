/*
 * mysqllib
 * 
 * Created on 19 November 2014 at 4:09 PM.
 */

package com.maulss.enhancedlib.database;

import com.maulss.enhancedlib.database.data.Data;
import com.maulss.enhancedlib.database.data.DataType;
import com.maulss.enhancedlib.database.data.StatType;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * A utility class used for management with {@link java.util.UUID}s,
 * specifically converting Strings to UUIDs and backwards.
 *
 * @author Maulss
 * @see    java.util.UUID
 */
public final class UUIDUtil {

	/* Disable instantiation */
	private UUIDUtil() {}

	/**
	 * Gets the String with length {@code 32} version of
	 * a {@link java.util.UUID} with all dashes removed.
	 *
	 * @param   id The {@link java.util.UUID} to parse
	 *
	 * @return  The String version of {@param id}.
	 * @see     java.util.UUID
	 */
	@NotNull
	public static synchronized String fromUuid(UUID id) {
		return fromUuid(String.valueOf(id));
	}

	/**
	 * Gets the String with length {@code 32} version of a
	 * {@link java.util.UUID#toString()} with all dashes removed.
	 *
	 * @param   id The {@link java.util.UUID#toString()} to parse
	 *
	 * @return  The String version of {@param id}.
	 */
	@NotNull
	public static synchronized String fromUuid(String id) {
		return id.replaceAll("-", "");
	}

	/**
	 * Gets the {@link java.util.UUID#fromString(String)}
	 * from a String by adding in dashes at appropriate
	 * places and creating a new UUID instance.
	 *
	 * @param   id The UUID in String form, without the dashes
	 *
	 * @return  A new instance of {@link java.util.UUID}.
	 * @see     java.util.UUID#fromString(String)
	 */
	@NotNull
	public static synchronized UUID fromString(String id) {
		return UUID.fromString(id.length() == 32 ?
				id.substring(0, 8) + "-" +
				id.substring(8, 12) + "-" +
				id.substring(12, 16) + "-" +
				id.substring(16, 20) + "-" +
				id.substring(20, 32) : id
		);
	}

	/**
	 * Checks whether the String provided is fit to be an
	 * actual {@link java.util.UUID}.
	 *
	 * @param   str The String to check
	 *
	 * @return  True if it meets the requirements of a UUID.
	 * @see     java.util.UUID
	 */
	public static synchronized boolean isUuid(String str) {

		// Check if str is natively in the form of a real UUID
		try {
			// noinspection ResultOfMethodCallIgnored
			UUID.fromString(str);
			return true;
		} catch (IllegalArgumentException ignored) {
			if (str.length() != 32) {
				return false;
			}

			// Also check if str can qualify for a real UUID provided the dashes are taken out
			if (StringUtils.countMatches(str, "-") != 4) {
				try {
					fromString(str);
					return true;
				} catch (IllegalArgumentException ignored1) {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	/**
	 * Checks whether {@param obj} is fit for properties of a {@link java.util.UUID}.
	 * If so, parses UUID to a String removing all dashes to make a 32-length String.
	 *
	 * @param   obj The object to check for
	 *
	 * @return  The parsed String to be used in the database.
	 * @see     #isUuid(String)
	 * @see     #fromString(String)
	 */
	@NotNull
	public static synchronized String checkForValidUuid(Object obj) {
		String str = String.valueOf(obj);
		return isUuid(str) ? fromUuid(str) : str;
	}

	/**
	 * Used as a global identifier for storing 32-length UUID-related objects without dashes.
	 *
	 * <table summary="">
	 *     <tr>
	 *         <td>Column</td>
	 *         <td>Data Type</td>
	 *         <td>Wildcards</td>
	 *     </tr>
	 *     <tr>
	 *         <td>UUID</td>
	 *         <td>VARCHAR(32)</td>
	 *         <td>PRIMARY KEY, NOT NULL, UNIQUE</td>
	 *     </tr>
	 * </table>
	 *
	 * @return The global identifier used for storing 32-length UUID-related objects without dashes
	 */
	@NotNull
	public static synchronized StatType getUserIdentifier() {
		return new StatType() {

			private static final long 	serialVersionUID 	= -9159090312444048776L;
			private static final String COLUMN 				= "UUID";

			@Override
			@NotNull
			public String getColumn() {
				return COLUMN;
			}

			@Override
			@NotNull
			public Data getData() {
				// Since all UUID-related values sent to the database will automatically have
				// its dashes removed, we can safely give the column type's (VARCHAR) length
				// a maximum of 32 characters to save space as that's all that will be needed.
				return new Data(COLUMN, DataType.VARCHAR.setAttributes(32)).key().notNull().unique();
			}

			@Override
			@NotNull
			public String toString() {
				return COLUMN;
			}
		};
	}
}