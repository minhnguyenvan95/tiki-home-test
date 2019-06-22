package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    private static final SimpleDateFormat usagePeriodDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private DateUtil() {
    }

    public static Date parseRecordDate(String dateString) throws ParseException {
        return usagePeriodDateFormat.parse(dateString);
    }

    public static String formatDate(Date date) {
        return usagePeriodDateFormat.format(date);
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillisecond = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillisecond, TimeUnit.MILLISECONDS);
    }
}
