package com.pahana.edu.billing.util;

public class ValidationUtil {

    /** Check null or empty string */
    public static boolean isEmpty(String s) {
        return (s == null || s.trim().isEmpty());
    }

    /** Check if string is numeric integer */
    public static boolean isInteger(String s) {
        if (isEmpty(s)) return false;
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Check if string is numeric double */
    public static boolean isDouble(String s) {
        if (isEmpty(s)) return false;
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Check if email is valid (very basic) */
    public static boolean isEmail(String s) {
        if (isEmpty(s)) return false;
        return s.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /** Password length policy (e.g. min 6 chars) */
    public static boolean isValidPassword(String s) {
        return (s != null && s.length() >= 6);
    }
}
