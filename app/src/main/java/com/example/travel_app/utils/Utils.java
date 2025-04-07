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
        if (pattern == null || pattern.isEmpty()) {
            pattern = "dd/MM/yyyy";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Trả về null nếu chuỗi không đúng định dạng
        }
    }
}
