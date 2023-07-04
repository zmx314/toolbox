package com.zxm.toolbox.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {
	public static Date getFirstDay(YearMonth ym) {
		if(ym == null)
			return null;
		LocalDate localDate = LocalDate.of(ym.getYear(), ym.getMonthValue(), 1);
		return Date.valueOf(localDate);
	}

	public static Date getLastDay(YearMonth ym) {
		if(ym == null)
			return null;
		LocalDate localDate = LocalDate.of(ym.getYear(), ym.getMonthValue(), 1);
		localDate = localDate.with(TemporalAdjusters.lastDayOfMonth());
		return Date.valueOf(localDate);
	}
	
	public static Date getFirstDay(Date date) {
		if(date == null)
			return null;
		LocalDate localDate = date.toLocalDate();
		localDate = localDate.withDayOfMonth(1);
		return Date.valueOf(localDate);
	}
	
	public static Date getLastDay(Date date) {
		if(date == null)
			return null;
		LocalDate localDate = date.toLocalDate();
		localDate = localDate.with(TemporalAdjusters.lastDayOfMonth());
		return Date.valueOf(localDate);
	}
	
	public static LocalDate toLocalDate(Date date) {
		if(date == null)
			return null;
		return date.toLocalDate();
	}
	
	public static Date toDate(LocalDate localDate) {
		if (null == localDate) {
            return null;
        }
        return Date.valueOf(localDate);
	}
	
	public static YearMonth toYearMonth(Date date) {
		if(date == null)
			return null;
		LocalDate local = toLocalDate(date);
		YearMonth ym = YearMonth.of(local.getYear(), local.getMonthValue());
		return ym;
	}
}
