/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.magmacore.initialization;

import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

public record PluginInitializationConfig(String displayName, String adminPermission, int totalSteps, List<String> dependencies) {
    public PluginInitializationConfig(String displayName, String adminPermission, int totalSteps) {
        this(displayName, adminPermission, totalSteps, List.of());
    }

    public List<String> resolveDependencies(JavaPlugin plugin) {
        if (this.dependencies != null && !this.dependencies.isEmpty()) {
            return this.dependencies;
        }
        return plugin.getDescription().getSoftDepend();
    }
}

