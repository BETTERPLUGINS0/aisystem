/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.world.WorldLoadEvent
 */
package com.magmaguy.betterstructures.config;

import com.magmaguy.magmacore.config.ConfigurationEngine;
import com.magmaguy.magmacore.config.ConfigurationFile;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class ValidWorldsConfig
extends ConfigurationFile {
    private static HashMap<World, Boolean> validWorlds = new HashMap();
    private static boolean whitelistNewWorlds;
    private static ValidWorldsConfig instance;

    public ValidWorldsConfig() {
        super("ValidWorlds.yml");
        instance = this;
    }

    public static void registerNewWorld(World world) {
        if (ValidWorldsConfig.instance.fileConfiguration.getKeys(true).contains("Valid worlds." + world.getName())) {
            validWorlds.put(world, ValidWorldsConfig.instance.fileConfiguration.getBoolean("Valid worlds." + world.getName()));
            return;
        }
        ConfigurationEngine.setBoolean(ValidWorldsConfig.instance.fileConfiguration, "Valid worlds." + world.getName(), whitelistNewWorlds);
        ConfigurationEngine.fileSaverOnlyDefaults(ValidWorldsConfig.instance.fileConfiguration, ValidWorldsConfig.instance.file);
        validWorlds.put(world, whitelistNewWorlds);
    }

    public static boolean isValidWorld(World world) {
        if (validWorlds.get(world) != null) {
            return validWorlds.get(world);
        }
        return false;
    }

    @Override
    public void initializeValues() {
        whitelistNewWorlds = ConfigurationEngine.setBoolean(this.fileConfiguration, "New worlds spawn structures", true);
        for (World world : Bukkit.getWorlds()) {
            ConfigurationEngine.setBoolean(this.fileConfiguration, "Valid worlds." + world.getName(), true);
        }
        ConfigurationSection validWorldsSection = this.fileConfiguration.getConfigurationSection("Valid worlds");
        ArrayList<String> enabledWorlds = new ArrayList<String>();
        for (String key : validWorldsSection.getKeys(false)) {
            if (!validWorldsSection.getBoolean(key)) continue;
            enabledWorlds.add(key);
        }
        for (World world : Bukkit.getWorlds()) {
            validWorlds.put(world, enabledWorlds.contains(world.getName()));
        }
        ConfigurationEngine.fileSaverOnlyDefaults(this.fileConfiguration, this.file);
    }

    public static HashMap<World, Boolean> getValidWorlds() {
        return validWorlds;
    }

    public static boolean isWhitelistNewWorlds() {
        return whitelistNewWorlds;
    }

    public static class ValidWorldsConfigEvents
    implements Listener {
        @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
        public void onWorldLoad(WorldLoadEvent event) {
            ValidWorldsConfig.registerNewWorld(event.getWorld());
        }
    }
}

