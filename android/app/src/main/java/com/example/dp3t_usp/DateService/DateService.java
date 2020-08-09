package com.example.dp3t_usp.DateService;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateService {
    public static final HashMap<String, Integer> MonthToNumber = new HashMap<String, Integer>() {{
        put("Jan", 0);
        put("Feb", 1);
        put("Mar", 2);
        put("Apr", 3);
        put("May", 4);
        put("Jun", 5);
        put("Jul", 6);
        put("Aug", 7);
        put("Sep", 8);
        put("Oct", 9);
        put("Nov", 10);
        put("Dec", 11);
    }};

    public static Date parseString(String dateS){
        if (dateS == null){
            return null;
        }
        String[] inputParts = dateS.split(" ");
        int day = Integer.parseInt(inputParts[2]);
        int month = MonthToNumber.get(inputParts[1]);
        int year = Integer.parseInt(inputParts[5]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

}
