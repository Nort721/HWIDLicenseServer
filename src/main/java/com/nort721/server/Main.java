package com.nort721.server;

import com.nort721.server.storage.files.Config;
import com.nort721.server.storage.licenses.MongoManager;
import com.nort721.server.utils.Logger;
import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {

    // connection port for the license server
    public static final String VERSION = "0.0.1";

    @Getter
    private static MongoManager mongoManager;
    public static final Logger logger = new Logger();
    public static Config config;

    public static void main(String[] args) {

        System.out.println("=+= =================== =+=");
        System.out.println("Hardware-based Licensing Server");
        System.out.println("Version: " + VERSION);
        System.out.println("=+= =================== =+=");
        System.out.println();

        config = new Config();

        Scanner scanner = new Scanner(System.in);

        if (config.getMongoString().equals("none")) {
            logger.logMessage("Enter database name: ", false);
            config.setDatabaseName(scanner.nextLine());
        }

        if (config.getMongoString().equals("none")) {
            logger.logMessage("Enter the mongoString: ", false);
            config.setMongoString(scanner.nextLine());
        }

        if (config.getCommunicationKey().equals("none")) {
            logger.logMessage("Enter a communication encryption key: ", false);
            config.setCommunicationKey(scanner.nextLine());
        }

        if (config.getPort() == 0) {
            logger.logMessage("Enter listening port: ", false);
            config.setPort(scanner.nextInt());
        }

        if (config.getMaxHWID() == 0) {
            logger.logMessage("Enter desired max hardware-id per license: ", false);
            config.setMaxHWID(scanner.nextInt());
        }

        Constants.init();

        // setup database connection
        mongoManager = new MongoManager();

        //MongoDB.addLicenseToDatabase("AAAA-BBBB-CCCC-DDDD");

        // create a socket server that listens to a port
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(config.getPort());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        logger.logAction("Listening for connections . . .");

        // listen to license requests all the time
        while (true) {

            try {

                // accepts incoming sockets
                Socket socket = serverSocket.accept();

                SocketHandler server = new SocketHandler(socket, logger);

                server.start();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    public static String getSelfPath() {
        String path = null;
        try {
            path = Config.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return path;
    }
}
