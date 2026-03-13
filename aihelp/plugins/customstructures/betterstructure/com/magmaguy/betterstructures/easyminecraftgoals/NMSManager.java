/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package com.magmaguy.betterstructures.easyminecraftgoals;

import com.magmaguy.betterstructures.easyminecraftgoals.NMSAdapter;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketEntityTracker;
import java.util.Objects;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class NMSManager {
    private static final String PACKAGE = "com.magmaguy.betterstructures.easyminecraftgoals.";
    public static Plugin pluginProvider;
    private static NMSAdapter adapter;
    private static boolean isEnabled;

    public static void initializeAdapter(Plugin plugin) {
        pluginProvider = plugin;
        String version = NMSManager.getServerVersion();
        if (version == null) {
            plugin.getLogger().warning("Server version is null.");
            return;
        }
        try {
            Object versionName = Objects.equals(version, "v1_20_R0") ? "com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.NMSAdapter" : (Objects.equals(version, "v1_21_R7") ? (NMSManager.isPaper() ? "com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_paper.NMSAdapter" : "com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.NMSAdapter") : PACKAGE + version + ".NMSAdapter");
            adapter = (NMSAdapter)Class.forName((String)versionName).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            plugin.getLogger().log(Level.INFO, "Supported server version detected: {0}", version);
            isEnabled = true;
            PacketEntityTracker.getInstance().initialize(plugin);
        } catch (ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "Class not found: {0}", e.getMessage());
        } catch (ReflectiveOperationException e) {
            plugin.getLogger().log(Level.SEVERE, "Error instantiating class: {0}", e.getMessage());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Unexpected error: {0}", e.getMessage());
        } finally {
            if (!isEnabled) {
                plugin.getLogger().log(Level.SEVERE, "Server version \"{0}\" is unsupported! Please check for updates!", version);
            }
        }
    }

    private static boolean isPaper() {
        try {
            Class.forName("io.papermc.paper.configuration.GlobalConfiguration");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static NMSAdapter getAdapter() {
        return adapter;
    }

    public static void shutdown() {
        PacketEntityTracker.getInstance().shutdown();
        isEnabled = false;
        adapter = null;
    }

    private static String getServerVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        if (packageName.contains("_R")) {
            return packageName.split("\\.")[3];
        }
        String versionString = Bukkit.getServer().getVersion();
        if (!versionString.contains("-")) {
            pluginProvider.getLogger().warning("Incompatible Minecraft version detected! [1] Package: " + packageName + " version: " + versionString + " ! Report this to the developer.");
            return null;
        }
        try {
            int minor;
            String justVersion = versionString.split("-")[0];
            int major = Integer.parseInt(justVersion.split("\\.")[1]);
            try {
                minor = Integer.parseInt(justVersion.split("\\.")[2]);
            } catch (Exception e) {
                minor = 0;
            }
            return NMSManager.getInternalsFromRevision(major, minor);
        } catch (Exception e) {
            pluginProvider.getLogger().warning("Incompatible Minecraft version detected! [2] Package: " + packageName + " version: " + versionString + " ! Report this to the developer.");
            return null;
        }
    }

    private static String getInternalsFromRevision(int major, int minor) {
        Object versionString = "v1_";
        if (major == 20) {
            versionString = (String)versionString + "20_";
            if (minor == 6) {
                return (String)versionString + "R4";
            }
        } else if (major == 21) {
            versionString = (String)versionString + "21_";
            if (minor == 0 || minor == 1) {
                return (String)versionString + "R1";
            }
            if (minor == 2 || minor == 3) {
                return (String)versionString + "R2";
            }
            if (minor == 4) {
                return (String)versionString + "R3";
            }
            if (minor == 5) {
                return (String)versionString + "R4";
            }
            if (minor == 6 || minor == 7 || minor == 8) {
                return (String)versionString + "R5";
            }
            if (minor == 9 || minor == 10) {
                return (String)versionString + "R6";
            }
            if (minor == 11) {
                return (String)versionString + "R7";
            }
        }
        pluginProvider.getLogger().warning("Incompatible Minecraft version detected! [3] Package: " + Bukkit.getServer().getClass().getPackage().getName() + " version: " + Bukkit.getServer().getVersion() + " and attempted to get " + (String)versionString + " ! Report this to the developer.");
        return null;
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    static {
        isEnabled = false;
    }
}

