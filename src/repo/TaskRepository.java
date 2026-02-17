package repo;


import domain.Task;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static java.util.stream.Collectors.*;


public class TaskRepository {

    private final Path dataFile;
    private TaskRepository repository;

    public TaskRepository(Path dataFile) {
        this.dataFile = dataFile;
    }

    private void ensureFile(Path dataFile){
        if (!Files.exists(dataFile)){
             writeJson("[]");
        }
    }


    public void save(String description) {

    }

    public List<Task> loadAll(){
        return List.of();
    }

    private void writeJson(String json) {
        try {
            // 1. Define o arquivo temporário (mesmo diretório)
            Path tmp = dataFile.resolveSibling("tasks.tmp");

            // 2. Escreve o conteúdo no temporário
            Files.writeString(tmp, json, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            // 3. Move para o arquivo final
            Files.move(tmp, dataFile,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);

        } catch (IOException e) {
            // 4. Se algo deu errado, lançar RuntimeException (ou logar)
            throw new RuntimeException("Falha ao salvar JSON em " + dataFile, e);
        }
    }

    public void saveAll(List<Task> tasks) {
        ensureFile(dataFile);

        String json = tasks.stream()
                .map(task -> String.format(
                        "{\"id\":%d,\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\"}",
                        task.getId(),
                        escapeJson(task.getDescription()),
                        task.getStatus().name().toLowerCase().replace('_', '-'),
                        task.getCreatedAt(),
                        task.getUpdatedAt()
                ))
                .collect(joining(",\n  ", "[\n  ", "\n]"));

        writeJson(json);
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        StringBuilder sb = new StringBuilder(value.length());
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"'  -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }
}
