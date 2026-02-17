import domain.Status;
import repo.TaskRepository;
import service.TaskService;

import java.nio.file.Path;
import java.util.Arrays;

public class Main {

    private static final String DATA_FILE = "tasks.json";

    public static void main(String[] args) throws IllegalAccessException {
        if (args.length == 0) {
            printUsage();
            return;
        }

        TaskService taskService = bootstrapService();
        String action = args[0];

        switch (action) {
            case "add" -> handleAdd(taskService, args);
            case "update" -> handleUpdate(taskService, args);
            case "delete" -> handleDelete(taskService, args);
            case "mark-in-progress" -> handleMarkInProgress(taskService, args);
            case "mark-done" -> handleMarkDone(taskService, args);
            case "list" -> handleList(taskService, args);
            default -> {
                System.err.println("Unknown command: " + action);
                printUsage();
            }
        }
    }

    private static TaskService bootstrapService() {
        TaskRepository repository = new TaskRepository(Path.of(DATA_FILE));
        return new TaskService(repository);
    }

    private static void handleAdd(TaskService service, String[] args) throws IllegalAccessException {
        if (args.length < 2) {
            System.err.println("Usage: task-cli add \"description\"");
            return;
        }
        String description = joinArgsFromIndex(args, 1);
        service.add(description);
    }

    private static void handleUpdate(TaskService service, String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: task-cli update <id> \"description\"");
            return;
        }
        long id = parseId(args[1]);
        String description = joinArgsFromIndex(args, 2);
        service.update(id, description);
    }

    private static void handleDelete(TaskService service, String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: task-cli delete <id>");
            return;
        }
        long id = parseId(args[1]);
        service.delete(id);
    }

    private static void handleMarkInProgress(TaskService service, String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: task-cli mark-in-progress <id>");
            return;
        }
        long id = parseId(args[1]);
        service.markStatus(id, Status.IN_PROGRESS);
    }

    private static void handleMarkDone(TaskService service, String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: task-cli mark-done <id>");
            return;
        }
        long id = parseId(args[1]);
        service.markStatus(id, Status.DONE);
    }

    private static void handleList(TaskService service, String[] args) {
        if (args.length == 1) {
            service.list(null);
            return;
        }

        String statusArg = args[1];
        Status status = switch (statusArg) {
            case "todo" -> Status.TODO;
            case "in-progress" -> Status.IN_PROGRESS;
            case "done" -> Status.DONE;
            default -> {
                System.err.println("Unknown status for list: " + statusArg);
                yield null;
            }
        };

        service.list(status);
    }

    private static long parseId(String raw) {
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id must be a number: " + raw, e);
        }
    }

    private static String joinArgsFromIndex(String[] args, int index) {
        return String.join(" ", Arrays.copyOfRange(args, index, args.length));
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  task-cli add \"description\"");
        System.out.println("  task-cli update <id> \"description\"");
        System.out.println("  task-cli delete <id>");
        System.out.println("  task-cli mark-in-progress <id>");
        System.out.println("  task-cli mark-done <id>");
        System.out.println("  task-cli list [todo|in-progress|done]");
    }
}
