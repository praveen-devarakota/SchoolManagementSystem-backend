package com.example.sms.util;

public class SchoolContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setSchoolCode(String schoolCode) {
        CONTEXT.set(schoolCode);
    }

    public static String getSchoolCode() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}