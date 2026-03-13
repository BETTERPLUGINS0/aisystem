/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 */
package com.magmaguy.magmacore.util;

import com.magmaguy.magmacore.util.Logger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ConfigurationLocation {
    private static final Set<String> notLoadedWorldNames = new HashSet<String>();

    private ConfigurationLocation() {
    }

    public static String deserialize(String worldName, double x, double y, double z, float pitch, float yaw) {
        return worldName + "," + x + "," + y + "," + z + "," + pitch + "," + yaw;
    }

    public static String deserialize(Location location) {
        return ConfigurationLocation.deserialize(Objects.requireNonNull(location.getWorld()).getName(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
    }

    public static Location serialize(String locationString) {
        return ConfigurationLocation.serialize(locationString, false);
    }

    public static Location serialize(String locationString, boolean silent) {
        float pitch;
        float yaw;
        double z;
        double y;
        double x;
        World world;
        block9: {
            if (locationString == null) {
                return null;
            }
            world = null;
            x = 0.0;
            y = 0.0;
            z = 0.0;
            yaw = 0.0f;
            pitch = 0.0f;
            try {
                String locationOnlyString = locationString.split(":")[0];
                String[] slicedString = locationOnlyString.split(",");
                if (slicedString.length == 6 || slicedString.length == 4) {
                    world = Bukkit.getWorld((String)slicedString[0]);
                    if (!(world != null || slicedString[0].equalsIgnoreCase("same_as_boss") || notLoadedWorldNames.contains(slicedString[0]) || silent)) {
                        notLoadedWorldNames.add(slicedString[0]);
                    }
                    x = Double.parseDouble(slicedString[1]);
                    y = Double.parseDouble(slicedString[2]);
                    z = Double.parseDouble(slicedString[3]);
                    if (slicedString.length > 4) {
                        yaw = Float.parseFloat(slicedString[4]);
                        pitch = Float.parseFloat(slicedString[5]);
                    } else {
                        yaw = 0.0f;
                        pitch = 0.0f;
                    }
                    break block9;
                }
                if (slicedString.length == 5) {
                    x = Double.parseDouble(slicedString[0]);
                    y = Double.parseDouble(slicedString[1]);
                    z = Double.parseDouble(slicedString[2]);
                    yaw = Float.parseFloat(slicedString[3]);
                    pitch = Float.parseFloat(slicedString[4]);
                    break block9;
                }
                throw new Exception();
            } catch (Exception ex) {
                if (locationString.equals("null")) {
                    return null;
                }
                Logger.warn("Attempted to deserialize an invalid location!");
                Logger.warn("Expected location format: worldname,x,y,z,pitch,yaw");
                Logger.warn("Actual location format: " + locationString);
                return null;
            }
        }
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String worldName(String locationString) {
        String locationOnlyString = locationString.split(":")[0];
        String[] slicedString = locationOnlyString.split(",");
        return slicedString[0];
    }

    public static Location serializeWithInstance(World instancedWorld, String locationString) {
        float pitch;
        float yaw;
        double z;
        double y;
        double x;
        block8: {
            if (locationString == null) {
                return null;
            }
            x = 0.0;
            y = 0.0;
            z = 0.0;
            yaw = 0.0f;
            pitch = 0.0f;
            try {
                String locationOnlyString = locationString.split(":")[0];
                String[] slicedString = locationOnlyString.split(",");
                if (slicedString.length == 6 || slicedString.length == 4) {
                    x = Double.parseDouble(slicedString[1]);
                    y = Double.parseDouble(slicedString[2]);
                    z = Double.parseDouble(slicedString[3]);
                    if (slicedString.length > 4) {
                        yaw = Float.parseFloat(slicedString[4]);
                        pitch = Float.parseFloat(slicedString[5]);
                    } else {
                        yaw = 0.0f;
                        pitch = 0.0f;
                    }
                    break block8;
                }
                if (slicedString.length == 5) {
                    x = Double.parseDouble(slicedString[0]);
                    y = Double.parseDouble(slicedString[1]);
                    z = Double.parseDouble(slicedString[2]);
                    yaw = Float.parseFloat(slicedString[3]);
                    pitch = Float.parseFloat(slicedString[4]);
                    break block8;
                }
                throw new Exception();
            } catch (Exception ex) {
                if (locationString.equals("null")) {
                    return null;
                }
                Logger.warn("Attempted to deserialize an invalid location!");
                Logger.warn("Expected location format: worldname,x,y,z,pitch,yaw");
                Logger.warn("Actual location format: " + locationString);
                return null;
            }
        }
        return new Location(instancedWorld, x, y, z, yaw, pitch);
    }
}

