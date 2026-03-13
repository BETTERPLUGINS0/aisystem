/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.handlers.bedrock;

import java.util.HashSet;
import java.util.UUID;
import net.advancedplugins.seasons.handlers.bedrock.BedrockListeners;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BedrockHandler {
    private final HashSet<UUID> bedrockPlayers = new HashSet();

    public BedrockHandler(JavaPlugin javaPlugin) {
        Bukkit.getServer().getPluginManager().registerEvents((Listener)new BedrockListeners(this), (Plugin)javaPlugin);
    }

    public HashSet<UUID> getBedrockPlayers() {
        return this.bedrockPlayers;
    }
}

