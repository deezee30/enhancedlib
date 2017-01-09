package com.maulss.enhancedlib.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public final class TimeUtil {

	/* Disable initialization */
	private TimeUtil() {}

	private static final long
			SECOND	= 1000L,
			MINUTE	= 60L * SECOND,
			HOUR	= 60L * MINUTE,
			DAY		= 24L * HOUR,
			MONTH	= 30L * DAY,
			YEAR	= 12L * MONTH;

	public static final SimpleDateFormat
			DATE_FORMAT_SHORT	= new SimpleDateFormat("dd/MM/yy @ h:mm a"),
			DATE_FORMAT_LONG	= new SimpleDateFormat("EEEEE, d MMMMM yyyy @ h:mm a");

	public static long parseDateToMillis(SimpleDateFormat format, String timestamp) {
		try {
			return format.parse(timestamp).toInstant().toEpochMilli();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return -1L;
	}

	public static long parseDateToMillis(SimpleDateFormat format, String timestamp, String fromTimeZone) {
		SimpleDateFormat f = (SimpleDateFormat) format.clone();
		f.setTimeZone(TimeZone.getTimeZone(fromTimeZone));
		return parseDateToMillis(f, timestamp);
	}

	public static String prettifyTime(long diff) {
		boolean ago = diff < 0;
		diff = Math.abs(diff);

		String what;
		double amount;

		if (diff < MINUTE) {
			return "Just now";
		} else if (diff < HOUR) {
			amount = diff / MINUTE;
			what = "minute";
		} else if (diff < DAY) {
			amount = diff / HOUR;
			what = "hour";
		} else if (diff < MONTH) {
			amount = diff / DAY;
			what = "day";
		} else if (diff < YEAR) {
			amount = diff / MONTH;
			what = "month";
		} else {
			amount = diff / YEAR;
			what = "year";
		}

		return String.format(
				"%s %s %s",
				(int) amount,
				// if quantity is not 1 then add plural "s"
				what + (amount != 1d ? "s" : ""),
				ago ? "ago" : "from now"
		);
	}
}
