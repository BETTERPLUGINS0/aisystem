/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.configuration.InvalidConfigurationException
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.ColorUtils;
import net.advancedplugins.as.impl.utils.FoliaScheduler;
import net.advancedplugins.as.impl.utils.LocalLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class DataHandler {
    private File file = null;
    private FileConfiguration fileConfiguration;
    private String fileName;
    private int loopNumber;
    private JavaPlugin instance;
    private List<Integer> activeTasks = new ArrayList<Integer>();
    private List<Listener> listeners = new ArrayList<Listener>();

    public DataHandler(File file, JavaPlugin javaPlugin) {
        this.file = file;
        this.instance = javaPlugin;
        this.populateFile(false);
    }

    public DataHandler(String string, JavaPlugin javaPlugin) {
        this(string, javaPlugin, false);
    }

    public DataHandler(String string, JavaPlugin javaPlugin, boolean bl) {
        this.instance = javaPlugin;
        if (string == null) {
            return;
        }
        this.fileName = string;
        this.populateFile(bl);
    }

    public DataHandler() {
    }

    private void populateFile(boolean bl) {
        this.fileConfiguration = new YamlConfiguration();
        File file = this.instance.getDataFolder();
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        if (this.file == null) {
            this.file = new File(this.instance.getDataFolder(), this.fileName + ".yml");
        }
        if (this.instance.getResource(this.fileName + ".yml") != null) {
            if (!this.file.exists()) {
                this.instance.saveResource(this.fileName + ".yml", true);
            }
        } else if (bl) {
            String[] stringArray = this.fileName.split("/");
            Object object = "";
            if (stringArray.length > 1) {
                for (int i = 0; i < stringArray.length - 1; ++i) {
                    object = (String)object + stringArray[i] + "/";
                    File file2 = new File(this.instance.getDataFolder(), (String)object);
                    if (file2.isDirectory()) continue;
                    file2.mkdirs();
                }
            }
            if (!this.file.exists()) {
                try {
                    this.file.createNewFile();
                } catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
        }
        try {
            this.fileConfiguration.load(this.file);
        } catch (IOException | InvalidConfigurationException throwable) {
            throwable.printStackTrace();
        }
    }

    public void reloadConfig() {
        this.file = new File(this.file.getPath());
        try {
            this.fileConfiguration = new YamlConfiguration();
            this.fileConfiguration.load((Reader)new InputStreamReader((InputStream)new FileInputStream(this.file), StandardCharsets.UTF_8));
        } catch (IOException | InvalidConfigurationException throwable) {
            throwable.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return this.fileConfiguration;
    }

    public File getFile() {
        return this.file;
    }

    public void save() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void saveAsync() {
        FoliaScheduler.runTaskAsynchronously((Plugin)this.instance, this::save);
    }

    public int increaseLoop() {
        ++this.loopNumber;
        return this.loopNumber;
    }

    public int getLoopNumber() {
        return this.loopNumber;
    }

    public void clearLoopNumer() {
        this.loopNumber = 0;
    }

    public boolean isPath(String string) {
        return this.fileConfiguration.isConfigurationSection(string);
    }

    public Set<String> getKeys(String string) {
        if (!this.fileConfiguration.isConfigurationSection(string)) {
            return Collections.emptySet();
        }
        return this.fileConfiguration.getConfigurationSection(string).getKeys(false);
    }

    public Set<String> getKeys(FileConfiguration fileConfiguration, String string) {
        return fileConfiguration.getConfigurationSection(string).getKeys(false);
    }

    public <T extends Enum<T>> T getEnum(String string, Class<T> clazz) {
        T t;
        String string2 = this.fileConfiguration.getString(string);
        try {
            t = Enum.valueOf(clazz, string2);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return t;
    }

    public LocalLocation getLocation(String string) {
        String string2 = this.getConfig().getString(string);
        return LocalLocation.getFromEncode(string2);
    }

    public void setLocation(String string, Location location) {
        this.setLocation(string, new LocalLocation(location));
    }

    public void setLocation(String string, LocalLocation localLocation) {
        this.getConfig().set(string, (Object)localLocation.getEncode());
    }

    public void tick() {
    }

    public void unload() {
        for (int n : this.activeTasks) {
            Bukkit.getScheduler().cancelTask(n);
        }
        for (Listener listener : this.listeners) {
            HandlerList.unregisterAll((Listener)listener);
        }
    }

    public UUID stringToId(String string) {
        return UUID.fromString(string);
    }

    public UUID getUUID(String string) {
        return UUID.fromString(this.getConfig().getString(string));
    }

    public int getInt(String string) {
        return this.getConfig().getInt(string);
    }

    public List<String> getStringList(String string) {
        List list = this.getConfig().getStringList(string);
        if (list.isEmpty()) {
            String string2 = this.getString(string);
            return string2 == null || string2.isEmpty() || string2.equalsIgnoreCase("[]") ? new ArrayList<String>() : new ArrayList<String>(Collections.singletonList(string2));
        }
        return list;
    }

    public String getString(String string) {
        return this.getConfig().getString(string);
    }

    public String getString(String string, String string2) {
        return this.getConfig().getString(string, string2);
    }

    public String getStringColored(String string) {
        return ColorUtils.format(this.getString(string));
    }

    public boolean getBoolean(String string, boolean bl) {
        return this.getConfig().getBoolean(string, bl);
    }

    public boolean getBoolean(String string) {
        return this.getConfig().getBoolean(string);
    }

    public <T> HashMap<String, T> sectionToMap(String string, Class<T> clazz) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        for (String string2 : this.getKeys(string)) {
            hashMap.put(string2, this.getConfig().get(string + "." + string2));
        }
        return hashMap;
    }

    public boolean isEnabled() {
        return this.getBoolean("enabled", true);
    }

    public void addTask(int n) {
        this.activeTasks.add(n);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, (Plugin)ASManager.getInstance());
        this.listeners.add(listener);
    }

    public String getFileName() {
        return this.fileName;
    }

    public JavaPlugin getInstance() {
        return this.instance;
    }

    public List<Integer> getActiveTasks() {
        return this.activeTasks;
    }

    public List<Listener> getListeners() {
        return this.listeners;
    }
}

