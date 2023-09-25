package com.nort721.server.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public class SimpleDate {
    private final int day, month, year;

    @Override
    public String toString() {
        return day + "-" + month + "-" + year;
    }

    public static SimpleDate fromString(String dateStr) {

        String[] date = new String[3];

        Arrays.fill(date, "");

        int index = 0;

        for (int i = 0; i < dateStr.length(); i++) {
            if (dateStr.charAt(i) == '-') {
                index++;
            } else
                date[index] += dateStr.charAt(i);
        }

        return new SimpleDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
    }
}
