package ru.zekoh;

import ru.zekoh.app.App;
import ru.zekoh.app.MyPreloader;
import com.sun.javafx.application.LauncherImpl;

public class Main {
    public static void main(String[] args) {
        LauncherImpl.launchApplication(App.class, MyPreloader.class, args);
    }
}
