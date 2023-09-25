package com.nort721.server;

import com.nort721.server.storage.licenses.MongoDB;
import com.nort721.server.utils.CommunicationUtil;
import com.nort721.server.utils.Logger;
import com.nort721.server.utils.crypto.AES;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class SocketHandler extends Thread {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    private final Logger logger;

    public SocketHandler(Socket socket, Logger logger) throws IOException {
        this.logger = logger;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        onSocketReceived();
    }

    public void onSocketReceived() {

        try {

            // log new connections
            logger.logMessage("Connection established with -> " + socket.getInetAddress().getHostAddress(), true);

            // string from received socket
            String messageFromClient = in.readLine();

            // check if the message is incorrect
            if (messageFromClient == null || messageFromClient.equals("")) {
                return;
            }

            String decryptedMessage = AES.decrypt(messageFromClient, Constants.CLIENT_COMMUNICATION_ENCRYPTION_KEY);
            assert decryptedMessage != null;

            String[] args = CommunicationUtil.getArgs(decryptedMessage, '|');

            String license = args[0];
            String hwid = args[1];

            // checking if license is whitelisted
            if (!MongoDB.containsLicense(license)) {
                Reply.reject(out);
                logger.logAction("rejected activation request for license: " + license + " [License is not whitelisted]");
                return;
            }

            List<String> hwids = MongoDB.getHWIDs(license);
            assert hwids != null;

            // checking if license exceeded the machines limit, only if the request came with a new hwid
            if (!hwids.contains(hwid) && (MongoDB.getHWIDCount(license) + 1) > Constants.MAX_HWID_PER_LICENSE) {
                Reply.reject(out);
                logger.logAction("rejected activation request for license: " + license + " [Passed HWID limit]");
                return;
            }

            /*
            This method will save this hwid under this license.
            Note: this method checks for doubles automatically
             */
            MongoDB.saveLicenseWithHWID(license, hwid);

            // he passed all checks so lets approve his activation request
            Reply.approve(out);
            logger.logAction("approved activation request for license: " + license);

        } catch (Exception e) {

            if (e.getMessage().contains("Connection reset")) {
                logger.logMessage("socket connection of host " + socket.getInetAddress().getHostAddress() + " has disconnected while trying to read a message ~ aborting task", true);
            }

        }
    }

    private void updateDatabaseData(String license, String hwid) {

    }
}

final class Reply {

    public static void reject(PrintWriter out) {
        out.println(AES.encrypt("rejected", Constants.CLIENT_COMMUNICATION_ENCRYPTION_KEY));
    }

    public static void approve(PrintWriter out) {
        out.println(AES.encrypt("approved", Constants.CLIENT_COMMUNICATION_ENCRYPTION_KEY));
    }
}
