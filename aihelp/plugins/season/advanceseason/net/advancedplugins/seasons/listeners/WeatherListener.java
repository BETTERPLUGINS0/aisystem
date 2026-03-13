/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.weather.ThunderChangeEvent
 *  org.bukkit.event.weather.WeatherChangeEvent
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.seasons.listeners;

import java.util.List;
import net.advancedplugins.seasons.Core;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class WeatherListener
implements Listener {
    @EventHandler(ignoreCancelled=true)
    public void onWeather(WeatherChangeEvent weatherChangeEvent) {
        World world = weatherChangeEvent.getWorld();
        if (this.isDisabled(world)) {
            return;
        }
        if (this.ignoreCancellation(world)) {
            return;
        }
        weatherChangeEvent.setCancelled(true);
    }

    @EventHandler(ignoreCancelled=true)
    public void onStorm(ThunderChangeEvent thunderChangeEvent) {
        World world = thunderChangeEvent.getWorld();
        if (this.isDisabled(world)) {
            return;
        }
        if (this.ignoreCancellation(world)) {
            return;
        }
        thunderChangeEvent.setCancelled(true);
    }

    private boolean isDisabled(World world) {
        return !Core.getWorldHandler().isWorldEnabled(world.getName());
    }

    private boolean ignoreCancellation(World world) {
        List list = world.getMetadata("ignoreWeatherCancellation");
        if (list.isEmpty()) {
            return false;
        }
        boolean bl = list.stream().filter(metadataValue -> metadataValue.getOwningPlugin() == Core.getInstance()).findFirst().map(MetadataValue::asBoolean).orElse(false);
        world.removeMetadata("ignoreWeatherCancellation", (Plugin)Core.getInstance());
        return bl;
    }
}

