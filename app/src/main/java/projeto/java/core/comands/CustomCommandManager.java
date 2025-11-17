package projeto.java.core.comands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomCommandManager {


    private static final Path FILE_PATH = Paths.get("custom_commands.json");

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final List<CustomCommand> commands = new ArrayList<>();

    public CustomCommandManager() {
        load();
    }

    private void load() {
        if (!Files.exists(FILE_PATH)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(FILE_PATH)) {
            Type listType = new TypeToken<List<CustomCommand>>() {}.getType();
            List<CustomCommand> loaded = gson.fromJson(reader, listType);
            if (loaded != null) {
                commands.clear();
                commands.addAll(loaded);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try (Writer writer = Files.newBufferedWriter(FILE_PATH)) {
            gson.toJson(commands, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<CustomCommand> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    public synchronized void addCommand(CustomCommand cmd) {
        commands.add(cmd);
        save();
    }
}
