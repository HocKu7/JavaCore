package main.common.solutions.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class JavaUtilDateUtils {

    private static final String PATTERN = "dd.MM.yyyy";

    private JavaUtilDateUtils() {

    }

    public static Date valueOf(String dateStr, String pattern) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.parse(dateStr);
        }
        catch (Exception e)
        {
            System.out.println("Data parse error");
            return null;
        }

    }

    public static Date valueOf(String dateStr) {
        return valueOf(dateStr, PATTERN);
    }

}
