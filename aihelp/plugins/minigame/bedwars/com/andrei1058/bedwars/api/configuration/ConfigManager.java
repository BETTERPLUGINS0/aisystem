/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.api.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
    private YamlConfiguration yml;
    private File config;
    private String name;
    private boolean firstTime = false;

    public ConfigManager(Plugin plugin, String name, String dir) {
        File d = new File(dir);
        if (!d.exists() && !d.mkdirs()) {
            plugin.getLogger().log(Level.SEVERE, "Could not create " + d.getPath());
            return;
        }
        this.config = new File(dir, name + ".yml");
        if (!this.config.exists()) {
            this.firstTime = true;
            plugin.getLogger().log(Level.INFO, "Creating " + this.config.getPath());
            try {
                if (!this.config.createNewFile()) {
                    plugin.getLogger().log(Level.SEVERE, "Could not create " + this.config.getPath());
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.yml = YamlConfiguration.loadConfiguration((File)this.config);
        this.yml.options().copyDefaults(true);
        this.name = name;
    }

    public void reload() {
        this.yml = YamlConfiguration.loadConfiguration((File)this.config);
    }

    public String stringLocationArenaFormat(Location loc) {
        return loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + (double)loc.getYaw() + "," + (double)loc.getPitch();
    }

    public String stringLocationConfigFormat(Location loc) {
        return loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + (double)loc.getYaw() + "," + (double)loc.getPitch() + "," + loc.getWorld().getName();
    }

    public void saveConfigLoc(String path, Location loc) {
        String data = loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + (double)loc.getYaw() + "," + (double)loc.getPitch() + "," + loc.getWorld().getName();
        this.yml.set(path, (Object)data);
        this.save();
    }

    public void saveArenaLoc(String path, Location loc) {
        String data = loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + (double)loc.getYaw() + "," + (double)loc.getPitch();
        this.yml.set(path, (Object)data);
        this.save();
    }

    public Location getConfigLoc(String path) {
        String d = this.yml.getString(path);
        if (d == null) {
            return null;
        }
        String[] data = d.replace("[", "").replace("]", "").split(",");
        return new Location(Bukkit.getWorld((String)data[5]), Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]));
    }

    public Location getArenaLoc(String path) {
        String d = this.yml.getString(path);
        if (d == null) {
            return null;
        }
        String[] data = d.replace("[", "").replace("]", "").split(",");
        return new Location(Bukkit.getWorld((String)this.name), Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]));
    }

    public Location convertStringToArenaLocation(String string) {
        String[] data = string.split(",");
        return new Location(Bukkit.getWorld((String)this.name), Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]));
    }

    public List<Location> getArenaLocations(String path) {
        ArrayList<Location> l = new ArrayList<Location>();
        for (String s : this.yml.getStringList(path)) {
            Location loc = this.convertStringToArenaLocation(s);
            if (loc == null) continue;
            l.add(loc);
        }
        return l;
    }

    public void set(String path, Object value) {
        this.yml.set(path, value);
        this.save();
    }

    public YamlConfiguration getYml() {
        return this.yml;
    }

    public void save() {
        try {
            this.yml.save(this.config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getList(String path) {
        return this.yml.getStringList(path).stream().map(s -> s.replace("&", "\u00a7")).collect(Collectors.toList());
    }

    public boolean getBoolean(String path) {
        return this.yml.getBoolean(path);
    }

    public int getInt(String path) {
        return this.yml.getInt(path);
    }

    public double getDouble(String path) {
        return this.yml.getDouble(path);
    }

    public String getString(String path) {
        return this.yml.getString(path);
    }

    public boolean isFirstTime() {
        return this.firstTime;
    }

    public boolean compareArenaLoc(Location l1, Location l2) {
        return l1.getBlockX() == l2.getBlockX() && l1.getBlockZ() == l2.getBlockZ() && l1.getBlockY() == l2.getBlockY();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

