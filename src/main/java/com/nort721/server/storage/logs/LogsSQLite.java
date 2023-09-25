package com.nort721.server.storage.logs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class LogsSQLite { // ToDo fix time out after reading data 3 times

    public static final String TABLE_LOGS = "logsTBL";

    public static final String COLUMN_LOG_ID = "logId";
    public static final String COLUMN_LOG = "log";

    private HikariDataSource hikari;

    private HikariConfig config;

    private final String CREATE_TABLE_QUESTION = "CREATE TABLE IF NOT EXISTS " +
            TABLE_LOGS + "(" + COLUMN_LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_LOG + " VARCHAR" + ")";

    public LogsSQLite() {
        connect();
    }

    public void connect() {

        reConnect();

        createTables();
    }

    public void reConnect() {

        try {
            if (hikari == null || hikari.getConnection() == null || hikari.getConnection().isClosed()) {
                config = new HikariConfig();
                config.setConnectionTimeout(40000);
                config.setConnectionTestQuery("SELECT 1");
                config.setPoolName("server_logs_pool"); // Pool Name
                config.setDriverClassName("org.sqlite.JDBC");
                final File file = new File("C:\\Users\\Nort\\Desktop\\", "database.db"); // location
                config.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath().replace("\\", "/"));

                hikari = new HikariDataSource(config);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void createTables() {

        try {

            Connection connection = hikari.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_TABLE_QUESTION);
            statement.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void saveLog(String log) {

        try {

            Connection connection = hikari.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + TABLE_LOGS + "(log) VALUES (?)");
            preparedStatement.setString(1, log);
            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public synchronized ArrayList<String> getLogs(int minRange, int maxRange) {

        ArrayList<String> logs = new ArrayList<>();

        try {

            Connection connection = hikari.getConnection();

//            PreparedStatement preparedStatement
//                    = connection.prepareStatement("SELECT * FROM " + TABLE_LOGS + " WHERE (" + COLUMN_LOG_ID + ") BETWEEN " + minRange + " AND " + maxRange + ";");

            PreparedStatement preparedStatement
                    = connection.prepareStatement("SELECT * FROM " + TABLE_LOGS + " WHERE " + COLUMN_LOG_ID + ">" + minRange + " AND " + COLUMN_LOG_ID + "<" +  maxRange);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                logs.add(resultSet.getString(COLUMN_LOG));
            }

            preparedStatement.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return logs;

    }
}
