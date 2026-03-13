/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package com.magmaguy.magmacore.config;

import com.magmaguy.magmacore.MagmaCore;
import com.magmaguy.magmacore.config.ConfigurationEngine;
import com.magmaguy.magmacore.config.CustomConfigFields;
import com.magmaguy.magmacore.util.Logger;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;

public class CustomConfig {
    private final HashMap<String, CustomConfigFields> customConfigFieldsHashMap = new HashMap();
    private final List customConfigFieldsArrayList = new ArrayList();
    private final String folderName;
    private final Class<? extends CustomConfigFields> customConfigFields;

    public CustomConfig(String folderName, Class<? extends CustomConfigFields> customConfigFields, CustomConfigFields schematicConfigField) {
        this.folderName = folderName;
        this.customConfigFields = customConfigFields;
        this.initialize(schematicConfigField);
    }

    public CustomConfig(String folderName, Class<? extends CustomConfigFields> customConfigFields) {
        this.folderName = folderName;
        this.customConfigFields = customConfigFields;
    }

    public CustomConfig(String folderName, String packageName, Class<? extends CustomConfigFields> customConfigFields) {
        this.folderName = folderName;
        this.customConfigFields = customConfigFields;
        String directory = MagmaCore.getInstance().getRequestingPlugin().getDataFolder().getAbsolutePath() + File.separatorChar + folderName;
        File file = Path.of((String)directory, (String[])new String[0]).toFile();
        if (!file.exists()) {
            file.mkdir();
        }
        if (packageName.isEmpty()) {
            return;
        }
        Reflections reflections = new Reflections(packageName, new Scanner[0]);
        try {
            HashSet<Class<? extends CustomConfigFields>> classSet = new HashSet<Class<? extends CustomConfigFields>>(reflections.getSubTypesOf(customConfigFields));
            classSet.forEach(aClass -> {
                try {
                    this.customConfigFieldsArrayList.add(aClass.newInstance());
                } catch (Exception ex) {
                    Logger.warn("Failed to generate plugin default classes for " + folderName + " ! This is very bad, warn the developer!");
                    ex.printStackTrace();
                }
            });
        } catch (Exception classSet) {
            // empty catch block
        }
        try {
            if (!Files.isDirectory(Paths.get(MagmaCore.getInstance().getRequestingPlugin().getDataFolder().getPath() + File.separatorChar + folderName, new String[0]), new LinkOption[0])) {
                this.generateFreshConfigurations();
                return;
            }
        } catch (Exception ex) {
            Logger.warn("Failed to generate plugin default files for " + folderName + " ! This is very bad, warn the developer!");
            ex.printStackTrace();
            return;
        }
        this.directoryCrawler(MagmaCore.getInstance().getRequestingPlugin().getDataFolder().getPath() + File.separatorChar + folderName);
        try {
            if (!this.customConfigFieldsArrayList.isEmpty()) {
                this.generateFreshConfigurations();
            }
        } catch (Exception ex) {
            Logger.warn("Failed to finish generating default plugin files for " + folderName + " ! This is very bad, warn the developer!");
            ex.printStackTrace();
        }
    }

    private void directoryCrawler(String path) {
        for (File file : Objects.requireNonNull(new File(path).listFiles())) {
            if (file.isFile()) {
                this.fileInitializer(file);
                continue;
            }
            if (!file.isDirectory()) continue;
            this.directoryCrawler(file.getPath());
        }
    }

    private void fileInitializer(File file) {
        boolean isPremade = false;
        for (Object object : this.customConfigFieldsArrayList) {
            try {
                Method getFilename = CustomConfigFields.class.getDeclaredMethod("getFilename", new Class[0]);
                if (!file.getName().equalsIgnoreCase((String)getFilename.invoke(object, new Object[0]))) continue;
                this.customConfigFieldsArrayList.remove(object);
                this.initialize((CustomConfigFields)object);
                isPremade = true;
                break;
            } catch (Exception ex) {
                Logger.warn("Failed to read plugin files for " + this.folderName + " ! This is very bad, warn the developer!");
                isPremade = true;
                ex.printStackTrace();
            }
        }
        if (!isPremade) {
            this.initialize(file);
        }
    }

    public HashMap<String, ? extends CustomConfigFields> getCustomConfigFieldsHashMap() {
        return this.customConfigFieldsHashMap;
    }

    public <V extends CustomConfigFields> void addCustomConfigFields(String filename, CustomConfigFields customConfigFields) {
        this.customConfigFieldsHashMap.put(filename, customConfigFields);
    }

    private void generateFreshConfigurations() {
        for (Object customConfigFields : this.customConfigFieldsArrayList) {
            this.initialize((CustomConfigFields)customConfigFields);
        }
    }

    private void initialize(CustomConfigFields customConfigFields) {
        File file = ConfigurationEngine.fileCreator(this.folderName, customConfigFields.getFilename());
        FileConfiguration fileConfiguration = ConfigurationEngine.fileConfigurationCreator(file);
        customConfigFields.setFile(file);
        customConfigFields.setFileConfiguration(fileConfiguration);
        customConfigFields.processConfigFields();
        ConfigurationEngine.fileSaverCustomValues(fileConfiguration, file);
        this.addCustomConfigFields(file.getName(), customConfigFields);
    }

    private void initialize(File file) {
        try {
            if (!file.getName().endsWith(".yml")) {
                return;
            }
            YamlConfiguration fileConfiguration = YamlConfiguration.loadConfiguration((File)file);
            Constructor<? extends CustomConfigFields> constructor = this.customConfigFields.getConstructor(String.class, Boolean.TYPE);
            CustomConfigFields instancedCustomConfigFields = constructor.newInstance(file.getName(), true);
            instancedCustomConfigFields.setFileConfiguration((FileConfiguration)fileConfiguration);
            instancedCustomConfigFields.setFile(file);
            instancedCustomConfigFields.processConfigFields();
            this.addCustomConfigFields(file.getName(), instancedCustomConfigFields);
        } catch (Exception ex) {
            Logger.warn("Bad constructor for file " + file.getName() + " ! You should probably delete that file.");
            ex.printStackTrace();
        }
    }
}

