/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.advancedplugins.seasons.handlers;

import com.google.common.collect.ImmutableMap;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.DataHandler;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.listeners.WeatherListener;
import net.advancedplugins.seasons.objects.CustomWeatherType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WeatherHandler
extends DataHandler {
    private final ImmutableMap<SeasonType, Integer> rainChance = ASManager.configToImmutableMap(this.getConfig(), "chanceOfPrecipitation", SeasonType::valueOf, Integer.class);

    public WeatherHandler(JavaPlugin javaPlugin) {
        super("weather", javaPlugin);
        if (!this.isEnabled()) {
            return;
        }
        this.registerListener(new WeatherListener());
        this.startWeatherTask();
    }

    private void startWeatherTask() {
        this.addTask(new BukkitRunnable(){

            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    if (!Core.getWorldHandler().isWorldEnabled(world.getName())) continue;
                    boolean bl = ASManager.doChancesPass(WeatherHandler.this.rainChance.get((Object)Core.getSeasonHandler().getSeason(world).getType()));
                    WeatherHandler.this.ignoreNextWeatherChange(world, true);
                    world.setStorm(bl);
                }
            }
        }.runTaskTimer((Plugin)Core.getInstance(), 20L, 800L).getTaskId());
    }

    public CustomWeatherType getWeather(String string) {
        SeasonType seasonType = Core.getSeasonHandler().getSeason(string).getType();
        World world = Bukkit.getWorld((String)string);
        Core cfr_ignored_0 = (Core)ASManager.getInstance();
        if (!Core.Bukkit) {
            return CustomWeatherType.SNOW;
        }
        if (world.hasStorm()) {
            if (seasonType.equals((Object)SeasonType.WINTER)) {
                return CustomWeatherType.SNOW;
            }
            return CustomWeatherType.RAIN;
        }
        if (ASManager.isDay(world.getTime())) {
            return CustomWeatherType.SUN;
        }
        return CustomWeatherType.MOON;
    }

    public boolean ignoreNextWeatherChange(World world, boolean bl) {
        if (!Core.getWorldHandler().isWorldEnabled(world.getName())) {
            return false;
        }
        world.setMetadata("ignoreWeatherCancellation", (MetadataValue)new FixedMetadataValue((Plugin)Core.getInstance(), (Object)bl));
        return true;
    }

    public ImmutableMap<SeasonType, Integer> getRainChance() {
        return this.rainChance;
    }
}

