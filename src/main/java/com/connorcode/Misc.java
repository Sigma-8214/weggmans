package com.connorcode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

public class Misc {
    public static String loadResourceString(String name) {
        return new BufferedReader(new InputStreamReader(Objects.requireNonNull(Misc.class.getClassLoader()
                .getResourceAsStream(name)))).lines()
                .collect(Collectors.joining("\n"));
    }

    public static int countDigits(int inp) {
        if (inp < 0) throw new RuntimeException("Not Implemented on Negatives");
        if (inp == 0) return 1;
        return (int) (Math.floor(Math.log10(inp)) + 1);
    }

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printHeader(String text, int totalWidth) {
        printHeader(text, totalWidth, '=');
    }

    public static void printHeader(String text, int totalWidth, char border) {
        String brd = String.valueOf(border);
        int startTextPos = totalWidth / 2 - text.length() / 2;

        System.out.println(brd.repeat(startTextPos) + text + brd.repeat(totalWidth - startTextPos - text.length()));
    }
}
