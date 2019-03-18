package com.gx.ksw.util;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 时间处理函数
 */
public class UtilTime {
    public static String dateAndTime(){
        String datetime = LocalDate.now().toString() + LocalTime.now().toString();
        datetime = datetime.replace("-", "").replace(":","").replace(".","");
        return datetime;
    }
}
