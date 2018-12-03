package ru.zekoh.core.synchronisation;

public class SData {
    static boolean inTheWork = false;
    public static boolean flag = true;
    public static boolean exitBool = false;


    public static boolean isInTheWork() {
        return inTheWork;
    }

    public static boolean isFlag() {
        return flag;
    }
}
