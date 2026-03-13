/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.localization;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.utils.text.Text;
import net.advancedplugins.localization.subclass.LocaleFile;
import org.bukkit.plugin.java.JavaPlugin;

public class LocaleHandler {
    private String locale;
    private String langFolder;
    private final JavaPlugin instance;
    private ImmutableMap<String, LocaleFile> localeMap;
    private static LocaleHandler handler = null;
    private String prefix;

    public LocaleHandler(JavaPlugin javaPlugin) {
        this.instance = javaPlugin;
        handler = this;
        this.localeMap = ImmutableMap.builder().build();
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String string) {
        this.prefix = this.color(this.getString(string));
    }

    public ImmutableSet<String> getAvailableLocales() {
        return this.localeMap.keySet();
    }

    public static LocaleHandler getHandler() {
        return handler;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String string) {
        this.locale = string;
        if (!this.localeMap.containsKey(string)) {
            try {
                this.instance.saveResource(this.langFolder + "/" + string + ".yml", true);
            } catch (Exception exception) {
                // empty catch block
            }
            this.localeMap = ImmutableMap.builder().putAll(this.localeMap).put(string, new LocaleFile(string, this.instance)).build();
        }
    }

    public LocaleHandler readLocaleFiles(JavaPlugin javaPlugin, String string) {
        this.langFolder = string;
        try {
            for (File file : new File(javaPlugin.getDataFolder(), string).listFiles()) {
                if (!file.getName().endsWith(".yml")) continue;
                String string2 = file.getName().replace(".yml", "");
                this.localeMap = ImmutableMap.builder().putAll(this.localeMap).put(string2, new LocaleFile(string2, this.instance)).build();
            }
            return this;
        } catch (Exception exception) {
            return this;
        }
    }

    private String color(String string) {
        return Text.modify(string.replace("%prefix%", this.getPrefix() != null ? this.getPrefix() : ""));
    }

    public LocaleFile getFile() {
        return this.localeMap.get(this.locale);
    }

    public String getString(String string, String string2) {
        return this.color(this.localeMap.get(this.locale).getLocaleConfig().getString(string, string2).replace("%prefix%", this.getPrefix() != null ? this.getPrefix() : ""));
    }

    public String getString(String string) {
        return this.color(this.localeMap.get(this.locale).getLocaleConfig().getString(string).replace("%prefix%", this.getPrefix() != null ? this.getPrefix() : ""));
    }

    public List<String> getStringList(String string) {
        return this.localeMap.get(this.locale).getLocaleConfig().getStringList(string).stream().map(this::color).collect(Collectors.toList());
    }

    public JavaPlugin getInstance() {
        return this.instance;
    }
}

