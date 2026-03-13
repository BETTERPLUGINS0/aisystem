/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package com.magmaguy.magmacore.config;

import com.magmaguy.magmacore.config.ConfigurationEngine;
import java.io.File;
import lombok.Generated;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class ConfigurationFile {
    protected final File file;
    protected FileConfiguration fileConfiguration;

    protected ConfigurationFile(String filename) {
        this.file = ConfigurationEngine.fileCreator(filename);
        this.fileConfiguration = YamlConfiguration.loadConfiguration((File)this.file);
        this.initializeValues();
        this.saveDefaults();
    }

    public abstract void initializeValues();

    public void saveDefaults() {
        ConfigurationEngine.fileSaverOnlyDefaults(this.fileConfiguration, this.file);
    }

    @Generated
    public FileConfiguration getFileConfiguration() {
        return this.fileConfiguration;
    }
}

