package io.bitnews.user_sdk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {

	public final static String SHORT_FORMAT = "yyyyMMdd";
	public final static String LONG_FORMAT = "yyyyMMddHHmmss";

	public static String format(Date date, String format) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat(format).format(date);
	}

	public static Date parse(String date, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(date);
	}
	
	/**
	 * 结束时间是否已经到期
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isEndTime(Date date) {

		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime endTime = LocalDateTime.ofInstant(date.toInstant(), zoneId).withMinute(0).withSecond(0)
				.withNano(0);
		LocalDateTime midnight = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
		return endTime.isBefore(midnight) || endTime.equals(midnight);
	}

	public static Long getTodayLeftSecondes() {
		LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		return ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);
	}

	public static int getPeriodDays(Date settleTime) {
		Instant instant = settleTime.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDate end = LocalDateTime.ofInstant(instant, zone).toLocalDate();
		LocalDate start = LocalDate.now();
		Period period = Period.between(start, end);
		return period.getDays() < 0 ? 0 : period.getDays();
	}
	
}
