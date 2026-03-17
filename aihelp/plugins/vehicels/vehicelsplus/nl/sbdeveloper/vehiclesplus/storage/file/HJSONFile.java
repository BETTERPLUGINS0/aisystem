/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus.storage.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import nl.sbdeveloper.vehiclesplus.utils.jackson.JacksonHelper;
import org.bukkit.plugin.java.JavaPlugin;
import org.hjson.JsonValue;
import org.hjson.Stringify;

public class HJSONFile {
    private File file;

    public HJSONFile(JavaPlugin javaPlugin, String string) {
        if (!javaPlugin.getDataFolder().exists() && !javaPlugin.getDataFolder().mkdir()) {
            javaPlugin.getLogger().severe("Couldn't generate the pluginfolder!");
            return;
        }
        this.file = new File(javaPlugin.getDataFolder(), string + ".hjson");
        File file = this.file.getParentFile();
        if (!file.exists() && !file.mkdirs()) {
            javaPlugin.getLogger().severe("Couldn't generate the required folder(s) for " + string + ".hjson!");
            return;
        }
        if (!this.file.exists()) {
            try {
                if (!this.file.createNewFile()) {
                    javaPlugin.getLogger().severe("Couldn't generate the " + string + ".hjson!");
                    return;
                }
                javaPlugin.getLogger().info("Generating the " + string + ".hjson file...");
                PrintWriter printWriter = new PrintWriter(this.file, StandardCharsets.UTF_8);
                printWriter.print("{");
                printWriter.print("}");
                printWriter.flush();
                printWriter.close();
            } catch (IOException iOException) {
                javaPlugin.getLogger().severe("Couldn't generate the " + string + ".hjson!");
            }
        }
    }

    public <T> T read(Class<T> clazz) {
        ObjectMapper objectMapper = JacksonHelper.getMapper(true);
        ObjectReader objectReader = objectMapper.reader();
        try (FileReader fileReader = new FileReader(this.file);){
            T t = objectReader.readValue(JsonValue.readHjson(fileReader).toString(), clazz);
            return t;
        }
    }

    public void write(Object object) {
        ObjectMapper objectMapper = JacksonHelper.getMapper(true);
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        try (FileWriter fileWriter = new FileWriter(this.file);){
            JsonValue.readHjson(objectWriter.writeValueAsString(object)).writeTo((Writer)fileWriter, Stringify.HJSON);
        }
    }
}

