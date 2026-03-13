/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.block.Block
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.handlers;

import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ThreadLocalRandom;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.DataHandler;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.listeners.CropGrowthListener;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CropsHandler
extends DataHandler {
    private final ImmutableMap<SeasonType, Double> growth;
    private final boolean winterGrowConditions = this.getBoolean("winterGrowth.basedOnConditions");
    private final int winterTempThreshold = this.getInt("winterGrowth.tempThreshold");
    private final double winterGrowth = this.getConfig().getDouble("winterGrowth.growthSpeed");

    public CropsHandler(JavaPlugin javaPlugin) {
        super("crops", javaPlugin);
        this.growth = ASManager.configToImmutableMap(this.getConfig(), "growth", SeasonType::valueOf, Double.class);
        if (!this.getBoolean("enabled")) {
            return;
        }
        Bukkit.getPluginManager().registerEvents((Listener)new CropGrowthListener(this), (Plugin)javaPlugin);
    }

    public int getBlockGrowth(Block block, int n, int n2) {
        SeasonType seasonType = Core.getSeasonHandler().getSeason(block.getWorld()).getType();
        double d = this.growth.get((Object)seasonType);
        if (d == 0.0) {
            return n;
        }
        if (seasonType.equals((Object)SeasonType.WINTER) && this.winterGrowConditions) {
            if (block.getWorld().getHighestBlockYAt(block.getLocation()) <= block.getY()) {
                return n;
            }
            if (Core.getTemperatureHandler().getLocationTemperature(block.getLocation(), false) < this.winterTempThreshold) {
                return n;
            }
        }
        int n3 = this.probabilisticMultiplier(1, d);
        return Math.min(n2, n + n3);
    }

    private int probabilisticMultiplier(int n, double d) {
        double d2 = (double)n * d;
        int n2 = (int)d2;
        double d3 = d2 - (double)n2;
        int n3 = n2;
        if (d >= 1.0 && ThreadLocalRandom.current().nextDouble() < d3) {
            ++n3;
        } else if (d < 1.0 && ThreadLocalRandom.current().nextDouble() < d) {
            n3 = 1;
        }
        return n3;
    }

    public ImmutableMap<SeasonType, Double> getGrowth() {
        return this.growth;
    }

    public boolean isWinterGrowConditions() {
        return this.winterGrowConditions;
    }

    public int getWinterTempThreshold() {
        return this.winterTempThreshold;
    }

    public double getWinterGrowth() {
        return this.winterGrowth;
    }
}

