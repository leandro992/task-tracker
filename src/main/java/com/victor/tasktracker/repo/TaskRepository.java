package com.victor.tasktracker.repo;

import com.victor.tasktracker.model.Task;
import com.victor.tasktracker.storage.JsonStorage;
import java.util.List;

public class TaskRepository {

    private final JsonStorage jsonStorage;

    public TaskRepository(JsonStorage jsonStorage) {
        this.jsonStorage = jsonStorage;
    }

    public List<Task> findAll() {
        return jsonStorage.readTasks();
    }

    public void saveAll(List<Task> tasks) {
        jsonStorage.writeTasks(tasks);
    }
}