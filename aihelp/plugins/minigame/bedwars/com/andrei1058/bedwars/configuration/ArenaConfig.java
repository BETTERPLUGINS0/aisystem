/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.configuration;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import com.andrei1058.bedwars.api.configuration.ConfigPath;
import com.andrei1058.bedwars.api.configuration.GameMainOverridable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ArenaConfig
extends ConfigManager {
    private List<String> cachedGameOverridables = new ArrayList<String>();

    public ArenaConfig(Plugin plugin, String name, String dir) {
        super(plugin, name, dir);
        YamlConfiguration yml = this.getYml();
        yml.options().header(plugin.getName() + " arena configuration file.\nDocumentation here: https://gitlab.com/andrei1058/BedWars1058/wikis/configuration/Arena-Configuration");
        yml.addDefault("group", (Object)"Default");
        yml.addDefault("display-name", (Object)"");
        yml.addDefault("minPlayers", (Object)2);
        yml.addDefault("maxInTeam", (Object)1);
        yml.addDefault("allowSpectate", (Object)true);
        yml.addDefault("spawn-protection", (Object)5);
        yml.addDefault("shop-protection", (Object)1);
        yml.addDefault("upgrades-protection", (Object)1);
        yml.addDefault("generator-protection", (Object)1);
        yml.addDefault("island-radius", (Object)17);
        yml.addDefault("worldBorder", (Object)300);
        yml.addDefault("y-kill-height", (Object)-1);
        yml.addDefault("max-build-y", (Object)180);
        yml.addDefault("disable-generator-for-empty-teams", (Object)false);
        yml.addDefault("disable-npcs-for-empty-teams", (Object)true);
        yml.addDefault("vanilla-death-drops", (Object)false);
        yml.addDefault("use-bed-hologram", (Object)true);
        yml.addDefault("allow-map-break", (Object)false);
        ArrayList<String> rules = new ArrayList<String>();
        rules.add("doDaylightCycle:false");
        rules.add("announceAdvancements:false");
        rules.add("doInsomnia:false");
        rules.add("doImmediateRespawn:true");
        rules.add("doWeatherCycle:false");
        rules.add("doFireTick:false");
        yml.addDefault("game-rules", rules);
        yml.options().copyDefaults(true);
        this.save();
        if (yml.get("spawnProtection") != null) {
            this.set("spawn-protection", yml.getInt("spawnProtection"));
            this.set("spawnProtection", null);
        }
        if (yml.get("shopProtection") != null) {
            this.set("shop-protection", yml.getInt("shopProtection"));
            this.set("shopProtection", null);
        }
        if (yml.get("upgradesProtection") != null) {
            this.set("upgrades-protection", yml.getInt("upgradesProtection"));
            this.set("upgradesProtection", null);
        }
        if (yml.get("islandRadius") != null) {
            this.set("island-radius", yml.getInt("islandRadius"));
        }
        if (yml.get("voidKill") != null) {
            this.set("voidKill", null);
        }
        this.set("enable-gen-split", null);
        this.cachedGameOverridables = this.getGameOverridables();
    }

    @NotNull
    private List<String> getGameOverridables() {
        ArrayList<String> paths = new ArrayList<String>();
        for (Field field : ConfigPath.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(GameMainOverridable.class)) continue;
            try {
                Object value = field.get(field);
                if (!(value instanceof String)) continue;
                paths.add((String)value);
            } catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        return paths;
    }

    public boolean isGameOverridable(String path) {
        return this.cachedGameOverridables.contains(path);
    }

    public Object getGameOverridableValue(String path) {
        if (!this.isGameOverridable(path)) {
            throw new RuntimeException("Given path is not game-overridable: " + path);
        }
        Object value = this.getYml().get(path, null);
        if (null == value) {
            return BedWars.config.getYml().get(path);
        }
        return value;
    }

    public Boolean getGameOverridableBoolean(String path) {
        Object value = this.getGameOverridableValue(path);
        return value instanceof Boolean ? (Boolean)value : false;
    }

    public String getGameOverridableString(String path) {
        Object value = this.getGameOverridableValue(path);
        return value instanceof String ? (String)value : "invalid";
    }
}

