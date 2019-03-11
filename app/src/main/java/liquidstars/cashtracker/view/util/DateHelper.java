package liquidstars.cashtracker.view.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    public static Date getCurrentDate(){
        return Calendar.getInstance().getTime();
    }

    public static SimpleDateFormat getSimpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public static String getCurrentDateString(){
        return getSimpleDateFormat().format(getCurrentDate());
    }

    public static String dateToString(Date date){
        return getSimpleDateFormat().format(date);
    }

    public static String getDateSubDaysString(int days){
        return getSimpleDateFormat().format(getDateSubDays(days));
    }

    public static Date getDateSubDays(int days){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - days);
        return c.getTime();
    }
}
