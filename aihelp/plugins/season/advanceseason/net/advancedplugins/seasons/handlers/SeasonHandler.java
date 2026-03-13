/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.handlers;

import java.util.HashMap;
import java.util.Optional;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.DynmapHook;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.biomes.BiomeUtils;
import net.advancedplugins.seasons.data.StorageHandler;
import net.advancedplugins.seasons.enums.Season;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.menus.SeasonShopMenu;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class SeasonHandler {
    private final HashMap<String, Season> season = new HashMap();

    public SeasonHandler(JavaPlugin javaPlugin) {
        for (String string : Core.getWorldHandler().getEnabledWorlds()) {
            this.setSeason(StorageHandler.getSeason(string), string);
        }
    }

    public void changeSeason(String string) {
        this.changeSeason(string, false);
    }

    public void changeSeason(String string, boolean bl) {
        SeasonType seasonType = SeasonType.valueOf(this.getSeason(string).name().split("_")[0]);
        this.setSeason(BiomeUtils.getTransitionEnum(seasonType, this.getSeason(string).getTransition() + 1), bl, string);
        Core.getBiomesHandler().getRenderHandler().refreshVisualBiomes(true);
    }

    public Season getSeason(String string) {
        return this.season.getOrDefault(string, Season.SPRING);
    }

    public Season getSeason(World world) {
        return this.getSeason(world.getName());
    }

    public Optional<Season> optionalSeason(String string) {
        return Optional.ofNullable(this.season.get(string));
    }

    public void setSeason(Season season, String string) {
        this.setSeason(season, false, string);
    }

    public void setSeason(Season season, boolean bl, String string) {
        boolean bl2 = this.getSeason(string).getType().equals((Object)SeasonType.WINTER) && !season.getType().equals((Object)SeasonType.WINTER);
        this.season.put(string, season);
        if (bl2) {
            this.toggleMapPluginGeneration(true);
        } else if (season.getType().equals((Object)SeasonType.WINTER)) {
            this.toggleMapPluginGeneration(false);
        }
        Core.getBiomesHandler().getRenderHandler().clearChunks();
        StorageHandler.setSeason(season, string);
        SeasonShopMenu.refreshSeasonMenus();
        if (bl) {
            Core.getCalendarHandler().recalc(string);
        }
    }

    private void toggleMapPluginGeneration(boolean bl) {
        if (HooksHandler.getHook(HookPlugin.DYNMAP) != null) {
            ((DynmapHook)HooksHandler.getHook(HookPlugin.DYNMAP)).setDynmapGeneration(bl);
        }
    }

    public HashMap<String, Season> getSeason() {
        return this.season;
    }
}

