/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.bukkit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.MetricsBase;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.charts.CustomChart;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Metrics {
    private final Plugin plugin;
    private final MetricsBase metricsBase;

    public Metrics(Plugin plugin, int n) {
        this.plugin = plugin;
        File file = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File file2 = new File(file, "config.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file2);
        if (!yamlConfiguration.isSet("serverUuid")) {
            yamlConfiguration.addDefault("enabled", (Object)true);
            yamlConfiguration.addDefault("serverUuid", (Object)UUID.randomUUID().toString());
            yamlConfiguration.addDefault("logFailedRequests", (Object)false);
            yamlConfiguration.addDefault("logSentData", (Object)false);
            yamlConfiguration.addDefault("logResponseStatusText", (Object)false);
            yamlConfiguration.options().header("bStats (https://bStats.org) collects some basic information for plugin authors, like how\nmany people use their plugin and their total player count. It's recommended to keep bStats\nenabled, but if you're not comfortable with this, you can turn this setting off. There is no\nperformance penalty associated with having metrics enabled, and data sent to bStats is fully\nanonymous.").copyDefaults(true);
            try {
                yamlConfiguration.save(file2);
            } catch (IOException iOException) {
                // empty catch block
            }
        }
        boolean bl = yamlConfiguration.getBoolean("enabled", true);
        String string2 = yamlConfiguration.getString("serverUuid");
        boolean bl2 = yamlConfiguration.getBoolean("logFailedRequests", false);
        boolean bl3 = yamlConfiguration.getBoolean("logSentData", false);
        boolean bl4 = yamlConfiguration.getBoolean("logResponseStatusText", false);
        boolean bl5 = false;
        try {
            bl5 = Class.forName("io.papermc.paper.threadedregions.RegionizedServer") != null;
        } catch (Exception exception) {
            // empty catch block
        }
        this.metricsBase = new MetricsBase("bukkit", string2, n, bl, this::appendPlatformData, this::appendServiceData, bl5 ? null : runnable -> Bukkit.getScheduler().runTask(plugin, runnable), () -> ((Plugin)plugin).isEnabled(), (string, throwable) -> this.plugin.getLogger().log(Level.WARNING, (String)string, (Throwable)throwable), string -> this.plugin.getLogger().log(Level.INFO, (String)string), bl2, bl3, bl4, false);
    }

    public void shutdown() {
        this.metricsBase.shutdown();
    }

    public void addCustomChart(CustomChart customChart) {
        this.metricsBase.addCustomChart(customChart);
    }

    private void appendPlatformData(JsonObjectBuilder jsonObjectBuilder) {
        jsonObjectBuilder.appendField("playerAmount", this.getPlayerAmount());
        jsonObjectBuilder.appendField("onlineMode", Bukkit.getOnlineMode() ? 1 : 0);
        jsonObjectBuilder.appendField("bukkitVersion", Bukkit.getVersion());
        jsonObjectBuilder.appendField("bukkitName", Bukkit.getName());
        jsonObjectBuilder.appendField("javaVersion", System.getProperty("java.version"));
        jsonObjectBuilder.appendField("osName", System.getProperty("os.name"));
        jsonObjectBuilder.appendField("osArch", System.getProperty("os.arch"));
        jsonObjectBuilder.appendField("osVersion", System.getProperty("os.version"));
        jsonObjectBuilder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }

    private void appendServiceData(JsonObjectBuilder jsonObjectBuilder) {
        jsonObjectBuilder.appendField("pluginVersion", this.plugin.getDescription().getVersion());
    }

    private int getPlayerAmount() {
        try {
            Method method = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers", new Class[0]);
            return method.getReturnType().equals(Collection.class) ? ((Collection)method.invoke(Bukkit.getServer(), new Object[0])).size() : ((Player[])method.invoke(Bukkit.getServer(), new Object[0])).length;
        } catch (Exception exception) {
            return Bukkit.getOnlinePlayers().size();
        }
    }
}

