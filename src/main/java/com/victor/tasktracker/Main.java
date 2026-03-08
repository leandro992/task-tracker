package com.victor.tasktracker;

import com.victor.tasktracker.cli.CommandHandler;
import com.victor.tasktracker.storage.JsonStorage;
import com.victor.tasktracker.util.ConsoleOutput;

public class Main {
   public static void main(String[] args) {
        try {
            JsonStorage jsonStorage = new JsonStorage();
            jsonStorage.initializeFileIfNeeded();

            CommandHandler commandHandler = new CommandHandler();
            commandHandler.handle(args);

        } catch (Exception exception) {
            ConsoleOutput.printError("Unexpected error: " + exception.getMessage());
        }
    }
}
