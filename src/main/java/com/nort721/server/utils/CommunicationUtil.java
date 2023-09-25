package com.nort721.server.utils;

import com.nort721.server.utils.crypto.HashUtil;

import java.util.ArrayList;

public final class CommunicationUtil {

    public static String[] getArgs(String input, char separator) {

        ArrayList<String> args = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {

            if (c == separator) {
                args.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }

        }

        args.add(sb.toString());

        String[] arr = new String[args.size()];
        args.toArray(arr);

        return arr;
    }

    public static String activateResponseGenerator(String license) {
        StringBuilder str = new StringBuilder();
        for (char c : license.toCharArray()) {
            str.append("a").append(HashUtil.hashString(c + "a", "SHA-256"));
        }
        return str.toString();
    }

}
