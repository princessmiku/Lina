package de.miku.lina.utils;

public class StringChecker {

    public static boolean isInteger(String number) {
        try {
            Integer.parseInt(number);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

}
