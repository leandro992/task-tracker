package com.victor.tasktracker.util;

import com.victor.tasktracker.model.Task;
import java.util.List;

public final class IdGenerator {

    private IdGenerator() {
    }

    public static int nextId(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return 1;
        }

        return tasks.stream()
                .map(Task::getId)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
}