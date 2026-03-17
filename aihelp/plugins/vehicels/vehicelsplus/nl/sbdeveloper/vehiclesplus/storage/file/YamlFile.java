/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.Configuration
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus.storage.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class YamlFile {
    private final JavaPlugin plugin;
    private final String name;
    private FileConfiguration fileConfiguration;
    private File file;

    public YamlFile(JavaPlugin javaPlugin, String string) {
        this.plugin = javaPlugin;
        this.name = string;
    }

    public void reloadFile() {
        if (this.file == null) {
            this.file = new File(this.plugin.getDataFolder(), this.name + ".yml");
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration((File)this.file);
        InputStream inputStream = this.plugin.getResource(this.name + ".yml");
        if (inputStream != null) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((Reader)new InputStreamReader(inputStream));
            this.fileConfiguration.setDefaults((Configuration)yamlConfiguration);
        }
    }

    public FileConfiguration getFile() {
        if (this.fileConfiguration == null) {
            this.reloadFile();
        }
        return this.fileConfiguration;
    }

    public void saveFile() {
        if (this.fileConfiguration == null || this.file == null) {
            return;
        }
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException iOException) {
            this.plugin.getLogger().log(Level.SEVERE, "Couldn't save the file " + this.name + ".yml.", iOException);
        }
    }

    public void loadDefaults() {
        if (this.file == null) {
            this.file = new File(this.plugin.getDataFolder(), this.name + ".yml");
        }
        if (!this.file.exists()) {
            this.plugin.saveResource(this.name + ".yml", false);
        }
    }
}

