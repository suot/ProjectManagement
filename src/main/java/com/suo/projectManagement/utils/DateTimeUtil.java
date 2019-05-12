package com.suo.projectManagement.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wuxu on 2018/6/1.
 */
public class DateTimeUtil {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date.getTime());

    }
}
