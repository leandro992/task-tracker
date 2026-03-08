package com.victor.tasktracker.storage;

import com.victor.tasktracker.util.ConsoleOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonStorage {

    private static final String FILE_NAME = "tasks.json";
    private static final String INITIAL_CONTENT = "[]";

    public void initializeFileIfNeeded() {
        Path filePath = Path.of(FILE_NAME);

        if (Files.exists(filePath)) {
            return;
        }

        try {
            Files.writeString(filePath, INITIAL_CONTENT, StandardCharsets.UTF_8);
            ConsoleOutput.printSuccess("File created successfully: " + FILE_NAME);
        } catch (IOException exception) {
            throw new RuntimeException("Could not create file " + FILE_NAME, exception);
        }
    }
}