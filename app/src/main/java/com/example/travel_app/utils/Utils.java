package com.example.travel_app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    // Hàm chuyển Date sang String với định dạng ngày/tháng/năm
    public static String dateToString(Date date, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            pattern = "dd/MM/yyyy";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return formatter.format(date);
    }

    // Hàm lấy ngày hiện tại dưới dạng String
    public String getCurrentDate(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            pattern = "dd/MM/yyyy";
        }
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return formatter.format(currentDate);
    }

    // Hàm chuyển String sang Date
    public static Date stringToDate(String dateString, String pattern) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        if (pattern == null || pattern.trim().isEmpty()) {
            pattern = "dd/MM/yyyy"; // hoặc định dạng mặc định của bạn
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
