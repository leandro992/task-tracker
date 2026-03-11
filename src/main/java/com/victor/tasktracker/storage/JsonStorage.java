package com.victor.tasktracker.storage;

import com.victor.tasktracker.model.Task;
import com.victor.tasktracker.model.TaskStatus;
import com.victor.tasktracker.util.ConsoleOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<Task> readTasks() {
        try {
            String content = Files.readString(Path.of(FILE_NAME), StandardCharsets.UTF_8).trim();

            if (content.isBlank() || content.equals("[]")) {
                return new ArrayList<>();
            }

            return parseTasks(content);
        } catch (IOException exception) {
            throw new RuntimeException("Could not read file " + FILE_NAME, exception);
        }
    }

    public void writeTasks(List<Task> tasks) {
        try {
            String json = toJson(tasks);
            Files.writeString(Path.of(FILE_NAME), json, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new RuntimeException("Could not write file " + FILE_NAME, exception);
        }
    }

    private List<Task> parseTasks(String json) {
        List<Task> tasks = new ArrayList<>();

        String normalized = json.trim();

        if (normalized.startsWith("[")) {
            normalized = normalized.substring(1);
        }

        if (normalized.endsWith("]")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        if (normalized.isBlank()) {
            return tasks;
        }

        List<String> objects = splitJsonObjects(normalized);

        for (String objectJson : objects) {
            Task task = parseTask(objectJson);
            tasks.add(task);
        }

        return tasks;
    }

    private List<String> splitJsonObjects(String content) {
        List<String> objects = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int braceLevel = 0;

        for (int i = 0; i < content.length(); i++) {
            char currentChar = content.charAt(i);

            if (currentChar == '{') {
                braceLevel++;
            }

            if (currentChar == '}') {
                braceLevel--;
            }

            current.append(currentChar);

            if (braceLevel == 0 && current.length() > 0) {
                String object = current.toString().trim();
                if (object.startsWith(",")) {
                    object = object.substring(1).trim();
                }
                if (!object.isBlank()) {
                    objects.add(object);
                }
                current.setLength(0);
            }
        }

        return objects;
    }

    private Task parseTask(String objectJson) {
        Task task = new Task();

        task.setId(Integer.parseInt(extractJsonValue(objectJson, "id")));
        task.setDescription(unescapeJson(extractJsonString(objectJson, "description")));
        task.setStatus(TaskStatus.fromValue(extractJsonString(objectJson, "status")));
        task.setCreatedAt(LocalDateTime.parse(extractJsonString(objectJson, "createdAt")));
        task.setUpdatedAt(LocalDateTime.parse(extractJsonString(objectJson, "updatedAt")));

        return task;
    }

    private String extractJsonValue(String json, String fieldName) {
        String pattern = "\"" + fieldName + "\":";
        int startIndex = json.indexOf(pattern);

        if (startIndex == -1) {
            throw new IllegalArgumentException("Field not found: " + fieldName);
        }

        startIndex += pattern.length();

        int endIndex = startIndex;
        while (endIndex < json.length() && json.charAt(endIndex) != ',' && json.charAt(endIndex) != '}') {
            endIndex++;
        }

        return json.substring(startIndex, endIndex).trim();
    }

    private String extractJsonString(String json, String fieldName) {
        String pattern = "\"" + fieldName + "\":\"";
        int startIndex = json.indexOf(pattern);

        if (startIndex == -1) {
            throw new IllegalArgumentException("Field not found: " + fieldName);
        }

        startIndex += pattern.length();

        StringBuilder value = new StringBuilder();
        boolean escaped = false;

        for (int i = startIndex; i < json.length(); i++) {
            char currentChar = json.charAt(i);

            if (escaped) {
                value.append(currentChar);
                escaped = false;
                continue;
            }

            if (currentChar == '\\') {
                escaped = true;
                continue;
            }

            if (currentChar == '"') {
                break;
            }

            value.append(currentChar);
        }

        return value.toString();
    }

    private String toJson(List<Task> tasks) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            jsonBuilder.append("  {\n");
            jsonBuilder.append("    \"id\": ").append(task.getId()).append(",\n");
            jsonBuilder.append("    \"description\": \"").append(escapeJson(task.getDescription())).append("\",\n");
            jsonBuilder.append("    \"status\": \"").append(task.getStatus().getValue()).append("\",\n");
            jsonBuilder.append("    \"createdAt\": \"").append(task.getCreatedAt()).append("\",\n");
            jsonBuilder.append("    \"updatedAt\": \"").append(task.getUpdatedAt()).append("\"\n");
            jsonBuilder.append("  }");

            if (i < tasks.size() - 1) {
                jsonBuilder.append(",");
            }

            jsonBuilder.append("\n");
        }

        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

    private String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    private String unescapeJson(String value) {
        return value
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}