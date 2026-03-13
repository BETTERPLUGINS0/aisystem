/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.seasons.api;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.api.biome.ASBiomeData;
import net.advancedplugins.seasons.biomes.BiomesHandler;
import net.advancedplugins.seasons.handlers.grass.GrassPattern;
import net.advancedplugins.seasons.handlers.grass.GrassPatternsUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class AdvancedSeasonsAPI {
    public Map<String, ASBiomeData> biomes() {
        return Core.getBiomesHandler().getBiomeData();
    }

    public Optional<ASBiomeData> biomeAt(Location location) {
        if (location == null) {
            return Optional.empty();
        }
        BiomesHandler biomesHandler = Core.getBiomesHandler();
        return biomesHandler.getAdvancedBiomeAt(location).map(advancedBiomeBase -> {
            String string = advancedBiomeBase.getName();
            return this.biomes().get(string);
        });
    }

    public void addNewGrassPattern(GrassPattern grassPattern) {
        GrassPatternsUtil.addNewGrassPattern(grassPattern);
    }

    public String getSeason(World world) {
        if (world == null) {
            return null;
        }
        return Core.getSeasonHandler().getSeason(world.getName()).getType().name();
    }

    public String getSeasonWithTransitions(World world) {
        if (world == null) {
            return null;
        }
        return Core.getSeasonHandler().getSeason(world.getName()).name();
    }

    public void setSeason(String string, World world) {
        if (world == null) {
            return;
        }
        Core.getSeasonHandler().setSeason(Core.getSeasonHandler().getSeason(string.toUpperCase(Locale.ROOT)), world.getName());
    }

    public int getTemperature(Player player) {
        if (player == null) {
            return 0;
        }
        return Core.getTemperatureHandler().getPlayerTemperatureMap().get((Object)player.getUniqueId()).displayTemperature;
    }

    public int getTemperature(Location location) {
        if (location == null) {
            return 0;
        }
        return Core.getTemperatureHandler().getLocationTemperature(location, true);
    }

    public void setTemperature(Player player, int n) {
        if (player == null) {
            return;
        }
        Core.getTemperatureHandler().setTemperature(player, n);
    }

    public void boostTemperature(Player player, int n) {
        if (player == null) {
            return;
        }
        Core.getTemperatureHandler().getPlayerTemperatureMap().get((Object)player.getUniqueId()).boostTemperature = n;
    }
}

