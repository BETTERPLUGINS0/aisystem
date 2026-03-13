/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils.hooks.holograms;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class HologramHandler {
    private final JavaPlugin plugin;

    public HologramHandler(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
    }

    public double getOffset() {
        return 1.0;
    }

    public String getName() {
        return "?";
    }

    public void createHologram(Location location, String string, String string2) {
    }

    public void updateHologram(String string, String string2) {
    }

    protected void removeFromList(String string) {
    }

    public void removeHologram(String string) {
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }
}

