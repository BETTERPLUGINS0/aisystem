/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.mtvehicles.core;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.MinecraftVersion;
import nl.mtvehicles.core.infrastructure.modules.CommandModule;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.ListenersModule;
import nl.mtvehicles.core.infrastructure.modules.LoopModule;
import nl.mtvehicles.core.infrastructure.modules.MetricsModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.utils.PluginUpdater;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
extends JavaPlugin {
    public static Main instance;
    public static final String configVersion = "2.5.6-b";
    public static final String messagesVersion = "2.5.8-dev7";

    public void onEnable() {
        instance = this;
        if (!new VersionModule().isSupportedVersion()) {
            return;
        }
        Main.logInfo("Plugin has been loaded!");
        if (VersionModule.isPreRelease) {
            Main.logWarning("Be aware: You are using a pre-release. It might not be stable and it's generally not advised to use it on a production server.");
        }
        Main.logInfo("--------------------------");
        Main.logInfo("Welcome by MTVehicles v" + VersionModule.getPluginVersion() + "!");
        Main.logInfo("Thanks for using our plugin.");
        Main.logInfo("--------------------------");
        this.disableNBTAPIVersionMessages();
        this.loadSkript();
        new CommandModule();
        new ListenersModule();
        new MetricsModule();
        new LoopModule();
        new ConfigModule();
        if (((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.AUTO_UPDATE)).booleanValue()) {
            PluginUpdater.checkNewVersion((CommandSender)this.getServer().getConsoleSender());
        }
    }

    public void onLoad() {
        new DependencyModule();
    }

    public void onDisable() {
        ConfigModule.vehicleDataConfig.saveToDisk();
        if (DependencyModule.isDependencyEnabled(SoftDependency.PLACEHOLDER_API)) {
            DependencyModule.placeholderAPI.unregisterOnDisable();
        }
    }

    public static String getFileAsString() {
        return String.valueOf(instance.getFile());
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, (Plugin)this);
    }

    private void disableNBTAPIVersionMessages() {
        MinecraftVersion.disableUpdateCheck();
    }

    private void loadSkript() {
        if (DependencyModule.isDependencyEnabled(SoftDependency.SKRIPT)) {
            try {
                DependencyModule.skript.load();
            } catch (Exception e) {
                Bukkit.getLogger().severe("MTVehicles could not load Skript addon. (Maybe you've just reloaded the plugin with PlugMan?)");
            }
        }
    }

    public static void disablePlugin() {
        Main.logSevere("Disabling the plugin (a critical bug might have occurred)...");
        instance.setEnabled(false);
    }

    public static void logInfo(String text) {
        instance.getLogger().info(text);
    }

    public static void logWarning(String text) {
        instance.getLogger().warning(text);
    }

    public static void logSevere(String text) {
        instance.getLogger().severe(text);
    }

    public static void schedulerRun(Runnable task) {
        Bukkit.getScheduler().runTask((Plugin)instance, task);
    }

    public static boolean isNotNull(Object ... objects) {
        for (Object object : objects) {
            if (object != null) continue;
            return false;
        }
        return true;
    }
}

