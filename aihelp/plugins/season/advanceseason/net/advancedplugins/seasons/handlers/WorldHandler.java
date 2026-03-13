/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.advancedplugins.seasons.Core;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldHandler {
    private final HashSet<String> enabledWorlds;

    public WorldHandler(JavaPlugin javaPlugin) {
        this.enabledWorlds = new HashSet(javaPlugin.getConfig().getStringList("worlds"));
    }

    public boolean isWorldEnabled(String string) {
        return this.enabledWorlds.contains(string);
    }

    public void addWorld(String string) {
        this.enabledWorlds.add(string);
        Core.getInstance().getConfig().set("worlds", new ArrayList<String>(this.enabledWorlds));
        Core.getInstance().saveConfig();
    }

    public List<World> getWorlds() {
        return this.enabledWorlds.stream().map(string -> Bukkit.getWorld((String)string)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void removeWorld(String string) {
        this.enabledWorlds.remove(string);
        Core.getInstance().getConfig().set("worlds", new ArrayList<String>(this.enabledWorlds));
        Core.getInstance().saveConfig();
    }

    public HashSet<String> getEnabledWorlds() {
        return this.enabledWorlds;
    }
}

