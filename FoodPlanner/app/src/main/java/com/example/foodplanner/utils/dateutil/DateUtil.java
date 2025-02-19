package com.example.foodplanner.utils.dateutil;

import java.util.Calendar;

public class DateUtil {
    public static long[] getCurrentWeekRange() {
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        if (today != Calendar.SATURDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -(today - Calendar.SATURDAY + 7) % 7);
        }
        long weekStart = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long weekEnd = calendar.getTimeInMillis();

        return new long[]{weekStart, weekEnd};
    }

    public static boolean isWithinCurrentWeek(int year, int month, int day, long weekStart, long weekEnd) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);
        long selectedTime = selectedDate.getTimeInMillis();
        return selectedTime >= weekStart && selectedTime <= weekEnd;
    }
}
