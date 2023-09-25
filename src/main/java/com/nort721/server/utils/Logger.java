package com.nort721.server.utils;

import com.nort721.server.Main;
import com.nort721.server.storage.logs.LogsSQLite;
import com.nort721.server.storage.logs.YamlStorage;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Logger {

    public static final String prefix = "Server > ";

    private final YamlStorage fileLogger;

    @Getter
    private LogsSQLite logsSQLite;

    public Logger() {
        String path = Main.getSelfPath();
        assert path != null;
        path = FilenameUtils.getPath(path);

        fileLogger = new YamlStorage("logs", path);
        //logsSQLite = new LogsSQLite();
    }

    public synchronized void logMessage(String msg, boolean newLine) {

        SimpleDate simpleDate = new SimpleDate(LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getYear());

        LocalTime localTime = LocalTime.now();

        // send to console
        if (newLine)
            System.out.println(prefix + simpleDate.toString() + " > " + localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond() + " > " + msg);
        else
            System.out.print(prefix + simpleDate.toString() + " > " + localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond() + " > " + msg);

        // write log to logs file
        ArrayList<String> logs = fileLogger.getStringList("logs.");

        if (logs == null)
            logs = new ArrayList<>();

        logs.add(simpleDate.toString() + " > " + localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond() + " > " + msg);

//        logsSQLite.saveLog(simpleDate.toString() + " > " + localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond() + " > " + msg);

        fileLogger.set("logs.", logs);
        fileLogger.save();
    }

    public synchronized void logAction(String action) {

        SimpleDate simpleDate = new SimpleDate(LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getYear());

        LocalTime localTime = LocalTime.now();

        // send to console
        System.out.println(prefix + action);

        // write log to logs file
        ArrayList<String> logs = fileLogger.getStringList("logs.");

        if (logs == null)
            logs = new ArrayList<>();

        logs.add(simpleDate.toString() + " > " + localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond() + " > " + action);

//        logsSQLite.saveLog(simpleDate.toString() + " > " + localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond() + " > " + action);

        fileLogger.set("logs.", logs);
        fileLogger.save();
    }
}
