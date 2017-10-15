package com.example.skarwa.letmeethappen.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.skarwa.letmeethappen.utils.Constants.DATE_PATTERN;

/**
 * Created by skarwa on 10/13/17.
 */

public class DateUtils {

    public static final String formatDateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);

        String stringDate = simpleDateFormat.format(date);
        System.out.println(stringDate);

        return stringDate;
    }

    public static final Date parseDatefromString(String stringDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);

        Date date = null;
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            Log.e("ERROR","Invalid Date String",e.getCause());
        }
        return date;
    }
}
