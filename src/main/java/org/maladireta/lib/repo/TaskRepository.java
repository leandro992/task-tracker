package org.maladireta.lib.repo;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.maladireta.lib.domain.Task;

public class TaskRepository {

    private final Path dataFile;
    private final Gson gson;

    public TaskRepository(Path dataFile, Gson gson) {
        this.dataFile = dataFile;
        this.gson = gson;
        ensureFile(dataFile, gson);
    }

    private void ensureFile(Path dataFile, Gson gson){
        if (!Files.exists(dataFile)){
             writeJson("[]");
        }
    }


    void saveAll(List<Task> tasks) throws IOException{
        String json = gson.toJson(tasks);
        writeJson(json);


    }

    List<Task> loadAll(){
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

}
