package ru.zekoh.core.printing;

import java.io.IOException;

public class Acquiring {
    public static boolean send(Double total) {
        boolean flag = false;
        try {
            Process proc = Runtime.getRuntime().exec("C:\\SC552\\UpWin.exe 1 " + total);
            proc.waitFor();
            proc.destroy();
            flag = false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return flag;
    }

    // Сверка итогов
    public static void report(){
        try {
            Process proc = Runtime.getRuntime().exec("C:\\SC552\\UpWin.exe 7");
            proc.waitFor();
            proc.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
