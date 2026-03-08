package com.victor.tasktracker.util;

public final class ConsoleOutput {

    private ConsoleOutput() {
    }

    public static void printInfo(String message) {
        System.out.println(message);
    }

    public static void printSuccess(String message) {
        System.out.println(message);
    }

    public static void printError(String message) {
        System.err.println(message);
    }
}