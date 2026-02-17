package service;

import domain.Status;
import domain.Task;
import java.time.Instant;
import java.util.List;
import repo.TaskRepository;

public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public void add(String description) throws IllegalAccessException {
        if (description == null || description.isBlank()){
            throw new IllegalAccessException("Description cannot be empty");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        List<Task> tasks = repository.loadAll();

        long nextId = tasks.stream()
                .mapToLong(Task::getId)
                .max()
                .orElse(0L) + 1L;

        Instant now = Instant.now();
        Task task = new Task(nextId, description.trim(), Status.TODO, now, now);

        tasks.add(task);
        repository.saveAll(tasks);

        System.out.printf("Task added successfully (ID: %d)%n", nextId);
    }

    public void update(long id, String description) {
        throw new UnsupportedOperationException("update not implemented yet");
    }

    public void delete(long id) {
        throw new UnsupportedOperationException("delete not implemented yet");
    }

    public void markStatus(long id, Status status) {
        throw new UnsupportedOperationException("markStatus not implemented yet");
    }

    public void list(Status status) {
        throw new UnsupportedOperationException("list not implemented yet");
    }
}
