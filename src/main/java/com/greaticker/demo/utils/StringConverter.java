package com.greaticker.demo.utils;

public class StringConverter {
    public static String longToStringConvert(Long l){
        if (l == null) {
            return null;
        } else {
            return String.valueOf(l);
        }
    }
}
