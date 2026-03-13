/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.utils.hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginHookInstance {
    public boolean isEnabled() {
        return false;
    }

    public String getName() {
        return "";
    }

    public Plugin getPluginInstance() {
        return Bukkit.getPluginManager().getPlugin(this.getName());
    }
}

