package com.indorse.blood.bank.service.impl.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date[] getMonthInterval(Date data) {

        Date[] dates = new Date[2];

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        start.setTime(data);
        start.set(Calendar.DAY_OF_MONTH, start.getActualMinimum(Calendar.DAY_OF_MONTH));
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);

        end.setTime(data);
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        dates[0] = start.getTime();
        dates[1] = end.getTime();
        System.out.println("start "+ start.getTime());
        System.out.println("end   "+ end.getTime());

        return dates;
    }

    public static Date[] getYearInterval(Date data) {

        Date[] dates = new Date[2];

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        start.setTime(data);
        start.set(Calendar.DAY_OF_YEAR, start.getActualMinimum(Calendar.DAY_OF_YEAR));
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);

        end.setTime(data);
        end.set(Calendar.DAY_OF_YEAR, end.getActualMaximum(Calendar.DAY_OF_YEAR));
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        dates[0] = start.getTime();
        dates[1] = end.getTime();

        return dates;
    }

    public static SimpleDateFormat getFormattedDate(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }
    public static void main(String[] args) {
        getMonthInterval(new Date());
    }
}
