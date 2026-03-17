/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package nl.mtvehicles.core.infrastructure.modules;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import lombok.Generated;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.infrastructure.enums.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

public class VersionModule {
    private static VersionModule instance;
    @Deprecated
    public static String pluginVersionString;
    public static boolean isPreRelease;
    public static boolean isDevRelease;
    private static String serverVersion;
    public static String serverSoftware;
    private Logger logger = Main.instance.getLogger();

    public VersionModule() {
        PluginDescriptionFile pdf = Main.instance.getDescription();
        pluginVersionString = pdf.getVersion();
        isPreRelease = pluginVersionString.toLowerCase().contains("pre") || pluginVersionString.toLowerCase().contains("rc") || pluginVersionString.toLowerCase().contains("dev");
        isDevRelease = pluginVersionString.toLowerCase().contains("dev");
        serverSoftware = Bukkit.getName();
        if (!serverSoftware.contains("Arclight")) {
            try {
                serverVersion = Bukkit.getServer().getMinecraftVersion();
            } catch (NoSuchMethodError e) {
                serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            }
        }
    }

    public static String getPluginVersion() {
        return pluginVersionString;
    }

    @VersionSpecific
    public static ServerVersion getServerVersion() {
        ServerVersion returns = null;
        switch (serverVersion) {
            case "1.12": 
            case "1.12.1": 
            case "1.12.2": 
            case "v1_12_R1": {
                returns = ServerVersion.v1_12;
                break;
            }
            case "1.13.1": 
            case "1.13.2": 
            case "v1_13_R2": {
                returns = ServerVersion.v1_13;
                break;
            }
            case "1.15": 
            case "1.15.1": 
            case "1.15.2": 
            case "v1_15_R1": {
                returns = ServerVersion.v1_15;
                break;
            }
            case "1.16.4": 
            case "1.16.5": 
            case "v1_16_R3": {
                returns = ServerVersion.v1_16;
                break;
            }
            case "1.17": 
            case "1.17.1": 
            case "v1_17_R1": {
                returns = ServerVersion.v1_17;
                break;
            }
            case "1.18": 
            case "1.18.1": 
            case "v1_18_R1": {
                returns = ServerVersion.v1_18_R1;
                break;
            }
            case "1.18.2": 
            case "v1_18_R2": {
                returns = ServerVersion.v1_18_R2;
                break;
            }
            case "1.19": 
            case "1.19.1": 
            case "1.19.2": 
            case "v1_19_R1": {
                returns = ServerVersion.v1_19_R1;
                break;
            }
            case "1.19.3": 
            case "v1_19_R2": {
                returns = ServerVersion.v1_19_R2;
                break;
            }
            case "1.19.4": 
            case "v1_19_R3": {
                returns = ServerVersion.v1_19_R3;
                break;
            }
            case "1.20": 
            case "1.20.1": 
            case "v1_20_R1": {
                returns = ServerVersion.v1_20_R1;
                break;
            }
            case "1.20.2": 
            case "1.20.3": 
            case "v1_20_R2": {
                returns = ServerVersion.v1_20_R2;
                break;
            }
            case "1.20.4": 
            case "1.20.5": 
            case "v1_20_R3": {
                returns = ServerVersion.v1_20_R3;
                break;
            }
            case "1.20.6": 
            case "v1_20_R4": {
                returns = ServerVersion.v1_20_R4;
                break;
            }
            case "1.21": 
            case "1.21.1": 
            case "1.21.2": 
            case "v1_21_R1": {
                returns = ServerVersion.v1_21_R1;
                break;
            }
            case "1.21.3": 
            case "v1_21_R2": {
                returns = ServerVersion.v1_21_R2;
                break;
            }
            case "1.21.4": 
            case "v1_21_R3": {
                returns = ServerVersion.v1_21_R3;
                break;
            }
            case "1.21.5": 
            case "v1_21_R4": {
                returns = ServerVersion.v1_21_R4;
                break;
            }
            case "1.21.6": 
            case "1.21.7": 
            case "1.21.8": 
            case "v1_21_R5": {
                returns = ServerVersion.v1_21_R5;
                break;
            }
            case "1.21.9": 
            case "1.21.10": 
            case "v1_21_R6": {
                returns = ServerVersion.v1_21_R6;
            }
        }
        return returns;
    }

    @VersionSpecific
    public boolean isSupportedVersion() {
        List<String> highestVersions = Arrays.asList("1.12.2", "1.13.2", "1.15.2", "1.16.5", "1.17.1", "1.18.2", "1.19.4", "1.20.6", "1.21.1", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10", "v1_21_R6", "v1_21_R5", "v1_21_R4", "v1_21_R3", "v1_21_R2", "v1_21_R1", "v1_20_R4", "v1_19_R3", "v1_18_R2", "v1_17_R1", "v1_16_R3", "v1_15_R1", "v1_13_R2", "v1_12_R1");
        if (!serverSoftware.contains("Arclight")) {
            try {
                serverVersion = Bukkit.getServer().getMinecraftVersion();
            } catch (NoSuchMethodError e) {
                serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            }
        }
        if (VersionModule.getServerVersion() == null) {
            this.logger.severe("--------------------------");
            this.logger.severe("Your Server version is not supported. The plugin will NOT load.");
            this.logger.severe("Check the supported versions here: https://wiki.mtvehicles.eu/faq.html");
            this.logger.severe("--------------------------");
            Main.disablePlugin();
            return false;
        }
        if (!highestVersions.contains(serverVersion)) {
            this.logger.warning("--------------------------");
            this.logger.warning("Your Server does not run the latest patch version (e.g. you may be running 1.18 instead of 1.18.2 etc...).");
            this.logger.warning("The plugin WILL load but it MAY NOT work properly. UPDATE.");
            this.logger.warning("Check the supported versions here: https://wiki.mtvehicles.eu/faq.html");
            this.logger.warning("--------------------------");
        } else if (!(serverSoftware.equals("Spigot") || serverSoftware.equals("Paper") || serverSoftware.equals("CraftBukkit"))) {
            this.logger.warning("--------------------------");
            this.logger.warning("Your Server is not running Spigot, nor Paper (" + serverSoftware + " detected).");
            this.logger.warning("The plugin WILL load but it MAY NOT work properly. Full support is guaranteed only on Spigot/Paper.");
            this.logger.warning("We'll be more than happy to help you on our Discord server (https://discord.gg/vehicle).");
            this.logger.warning("--------------------------");
        }
        return true;
    }

    @Generated
    public static VersionModule getInstance() {
        return instance;
    }

    @Generated
    public static void setInstance(VersionModule instance) {
        VersionModule.instance = instance;
    }
}

