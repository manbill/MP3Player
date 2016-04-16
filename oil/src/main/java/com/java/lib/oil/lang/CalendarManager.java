package com.java.lib.oil.lang;

import com.java.lib.oil.GlobalMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liutiantian on 2016-03-26.
 */
public class CalendarManager {
    private static CalendarManager mInstance;

    public static CalendarManager getInstance() {
        if (mInstance == null) {
            synchronized (CalendarManager.class) {
                if (mInstance == null) {
                    mInstance = new CalendarManager();
                }
            }
        }
        return mInstance;
    }

    private Calendar calendar;

    private CalendarManager() {
        this.calendar = Calendar.getInstance();
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int get(Date date, int field) {
        this.calendar.setTime(date);
        return this.calendar.get(field);
    }

    public String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format, Locale.CHINA).format(date);
    }

    public String transferDate(String sDate, String from, String to) {
        if (GlobalMethods.getInstance().checkEqual(from, to)) {
            return sDate;
        }
        if (sDate == null || from == null || to == null) {
            return null;
        }
        SimpleDateFormat fromFormat = new SimpleDateFormat(from, Locale.CHINA);
        try {
            Date date = fromFormat.parse(sDate);
            SimpleDateFormat toFormat = new SimpleDateFormat(to, Locale.CHINA);
            return toFormat.format(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Date offset(Date curDate, int offset) {
        this.calendar.setTime(curDate);
        this.calendar.add(Calendar.DATE, offset);
        return this.calendar.getTime();
    }

    public Date before(Date curDate) {
        return offset(curDate, -1);
    }

    public Date after(Date curDate) {
        return offset(curDate, +1);
    }
}
