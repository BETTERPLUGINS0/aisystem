/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.Configuration
 *  org.bukkit.configuration.file.FileConfiguration
 */
package com.magmaguy.magmacore.config;

import com.magmaguy.magmacore.util.Logger;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

public class UnusedNodeHandler {
    private UnusedNodeHandler() {
    }

    public static Configuration clearNodes(FileConfiguration configuration) {
        if (configuration.getDefaults() == null) {
            return configuration;
        }
        for (String actual : configuration.getKeys(false)) {
            boolean keyExists = false;
            for (String defaults : configuration.getDefaults().getKeys(true)) {
                if (!actual.equals(defaults)) continue;
                keyExists = true;
                break;
            }
            if (keyExists) continue;
            configuration.set(actual, null);
            Logger.info("Deleting unused config values.");
        }
        return configuration;
    }
}

