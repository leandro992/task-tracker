import com.victor.tasktracker.util.ConsoleOutput;

public class CommandHandler {

    public void handle(String[] args) {
        if (args == null || args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];

        switch (command) {
            case "add":
                ConsoleOutput.printInfo("Command recognized: add");
                break;

            case "update":
                ConsoleOutput.printInfo("Command recognized: update");
                break;

            case "delete":
                ConsoleOutput.printInfo("Command recognized: delete");
                break;

            case "mark-in-progress":
                ConsoleOutput.printInfo("Command recognized: mark-in-progress");
                break;

            case "mark-done":
                ConsoleOutput.printInfo("Command recognized: mark-done");
                break;

            case "list":
                ConsoleOutput.printInfo("Command recognized: list");
                break;

            default:
                ConsoleOutput.printError("Unknown command: " + command);
                printUsage();
        }
    }

    private void printUsage() {
        ConsoleOutput.printInfo("Usage:");
        ConsoleOutput.printInfo("  task-cli add \"Task description\"");
        ConsoleOutput.printInfo("  task-cli update <id> \"New description\"");
        ConsoleOutput.printInfo("  task-cli delete <id>");
        ConsoleOutput.printInfo("  task-cli mark-in-progress <id>");
        ConsoleOutput.printInfo("  task-cli mark-done <id>");
        ConsoleOutput.printInfo("  task-cli list");
        ConsoleOutput.printInfo("  task-cli list done");
        ConsoleOutput.printInfo("  task-cli list todo");
        ConsoleOutput.printInfo("  task-cli list in-progress");
    }
}