package com.nort721.server;

public final class Constants {
    public static String CLIENT_COMMUNICATION_ENCRYPTION_KEY = "";
    public static int MAX_HWID_PER_LICENSE = 1;
    public static int PORT;

    public static void init() {
        CLIENT_COMMUNICATION_ENCRYPTION_KEY = Main.config.getCommunicationKey();
        MAX_HWID_PER_LICENSE = Main.config.getMaxHWID();
        PORT = Main.config.getPort();
    }
}
