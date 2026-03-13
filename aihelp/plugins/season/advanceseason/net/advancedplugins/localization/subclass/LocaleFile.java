/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.localization.subclass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import net.advancedplugins.localization.LocaleHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LocaleFile {
    private File file;
    private final String locale;
    private FileConfiguration configuration = null;

    public LocaleFile(String string, JavaPlugin javaPlugin) {
        this.locale = string;
        this.saveFile(javaPlugin);
    }

    public String getLocale() {
        return this.locale;
    }

    public FileConfiguration getLocaleConfig() {
        if (this.configuration == null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(this.file);
                InputStreamReader inputStreamReader = new InputStreamReader((InputStream)fileInputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                this.configuration = YamlConfiguration.loadConfiguration((Reader)bufferedReader);
            } catch (Exception exception) {
                LocaleHandler.getHandler().getInstance().getLogger().warning("Failed to load locale " + this.locale);
                exception.printStackTrace();
                return null;
            }
        }
        return this.configuration;
    }

    public FileConfiguration getConfiguration() {
        return this.configuration;
    }

    private void saveFile(JavaPlugin javaPlugin) {
        File file = new File(javaPlugin.getDataFolder(), "lang/");
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        this.file = new File(javaPlugin.getDataFolder(), "lang/" + this.locale + ".yml");
        if (!this.file.exists()) {
            InputStream inputStream = javaPlugin.getResource("lang/" + this.locale + ".yml");
            try {
                this.file.createNewFile();
                byte[] byArray = new byte[inputStream.available()];
                inputStream.read(byArray);
                FileOutputStream fileOutputStream = new FileOutputStream(this.file);
                ((OutputStream)fileOutputStream).write(byArray);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

