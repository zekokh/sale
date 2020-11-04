package ru.zekoh.core.printing;

import java.io.*;

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

    // построчное считывание файла
    public static String readPFile() {
        try {
            File file = new File("C:\\SC552\\p");
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            //BufferedReader reader = new BufferedReader(fr);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("C:\\SC552\\p"), "UTF-8"));
            // считаем сначала первую строку
            String line = reader.readLine();
            String text = "";
            while (line != null) {
                System.out.println(line);
                // считываем остальные строки в цикле
                line = reader.readLine();
                text += line+"\n";
            }
            System.out.println("Печатаем на чеке чек Сбербанк терминала: "+ text);
            return text;
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при печати чеке от Сбербанк терминала");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Ошибка при печати чеке от Сбербанк терминала");
            e.printStackTrace();
        }
        return "";
    }
}
