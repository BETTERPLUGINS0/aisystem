/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.configuration;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import java.util.Arrays;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class SignsConfig
extends ConfigManager {
    public SignsConfig(Plugin plugin, String name, String dir) {
        super(plugin, name, dir);
        YamlConfiguration yml = this.getYml();
        yml.addDefault("format", Arrays.asList("&a[arena]", "", "&2[on]&9/&2[max] &7([type])", "[status]"));
        yml.addDefault("status-block.waiting.material", (Object)BedWars.getForCurrentVersion("STAINED_CLAY", "STAINED_CLAY", "GREEN_CONCRETE"));
        yml.addDefault("status-block.waiting.data", (Object)5);
        yml.addDefault("status-block.starting.material", (Object)BedWars.getForCurrentVersion("STAINED_CLAY", "STAINED_CLAY", "YELLOW_CONCRETE"));
        yml.addDefault("status-block.starting.data", (Object)14);
        yml.addDefault("status-block.playing.material", (Object)BedWars.getForCurrentVersion("STAINED_CLAY", "STAINED_CLAY", "RED_CONCRETE"));
        yml.addDefault("status-block.playing.data", (Object)4);
        yml.addDefault("status-block.restarting.material", (Object)BedWars.getForCurrentVersion("STAINED_CLAY", "STAINED_CLAY", "RED_CONCRETE"));
        yml.addDefault("status-block.restarting.data", (Object)4);
        yml.options().copyDefaults(true);
        this.save();
        if (yml.getStringList("format").size() < 4) {
            this.set("format", yml.getStringList("format").subList(0, 3));
        }
    }
}

