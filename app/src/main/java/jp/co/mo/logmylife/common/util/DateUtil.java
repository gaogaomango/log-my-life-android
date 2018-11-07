package jp.co.mo.logmylife.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static final String FORMAT_YYYYMMDDHHMMSS = "YYYY-MM-DD HH:MM:SS";
    public static final String FORMAT_DIVIDE_SLUSH_DDMMYYY = "dd/MM/yyyy";
    public static final String FORMAT_DIVIDE_HYPEHEN_DDMMYYY = "dd-MM-yyyy";

    public static String Epoch2DateString(String epochSeconds, String formatString) {
        Date updateDate = new Date(Long.parseLong(epochSeconds));
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(updateDate);
    }

    public static Calendar Epoch2Calender(String epochSeconds) {
        Date updateDate = new Date(Long.parseLong(epochSeconds));
        Calendar cal = Calendar.getInstance();
        cal.setTime(updateDate);

        return cal;
    }

    public static String todayDateString() {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DIVIDE_SLUSH_DDMMYYY, Locale.getDefault());

        return format.toString();
    }

    public static long getDate(String day) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DIVIDE_SLUSH_DDMMYYY, Locale.getDefault());
        Date date = new Date();
        try{
            date = format.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

}
