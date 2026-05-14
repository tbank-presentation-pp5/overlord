package ru.pp.gamma.overlord.common.util;

public class CommonUtil {

    public static boolean isLongNumber(String number) {
        try {
            Long.parseLong(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
