/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.magmacore.initialization;

import com.magmaguy.magmacore.initialization.PluginInitializationConfig;
import com.magmaguy.magmacore.initialization.PluginInitializationManager;
import com.magmaguy.magmacore.initialization.PluginInitializationProgressBar;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginInitializationContext {
    private final JavaPlugin plugin;
    private final PluginInitializationConfig config;

    PluginInitializationContext(JavaPlugin plugin, PluginInitializationConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public PluginInitializationConfig getConfig() {
        return this.config;
    }

    public void step(String description) {
        PluginInitializationProgressBar.step(this.plugin, description);
    }

    public void status(String description) {
        PluginInitializationProgressBar.status(this.plugin, description);
    }

    public boolean isShutdownRequested() {
        return PluginInitializationManager.isShutdownRequested(this.plugin);
    }
}

