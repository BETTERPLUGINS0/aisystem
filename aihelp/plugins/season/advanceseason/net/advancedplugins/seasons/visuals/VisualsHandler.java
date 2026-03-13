/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.visuals;

import java.util.HashMap;
import java.util.Map;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.DataHandler;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.enums.Season;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.visuals.type.ApplesDroppingVisual;
import net.advancedplugins.seasons.visuals.type.AuroraVisual;
import net.advancedplugins.seasons.visuals.type.BlizzardVisual;
import net.advancedplugins.seasons.visuals.type.DefaultFallingLeafVisual;
import net.advancedplugins.seasons.visuals.type.FallingSnowVisual;
import net.advancedplugins.seasons.visuals.type.IVisualType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class VisualsHandler
extends DataHandler {
    private HashMap<IVisualType, Integer> visualsRegistry = new HashMap();

    public VisualsHandler(JavaPlugin javaPlugin) {
        super("visuals", javaPlugin);
        if (!this.isEnabled()) {
            return;
        }
        this.registry();
        this.addTask(Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)javaPlugin, this::tick, 40L, 40L).getTaskId());
        this.addTask(Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)javaPlugin, () -> this.visualsRegistry.keySet().forEach(IVisualType::tick), 10L, 10L).getTaskId());
        this.addTask(Bukkit.getScheduler().runTaskTimer((Plugin)javaPlugin, () -> this.visualsRegistry.keySet().forEach(IVisualType::tickSync), 10L, 10L).getTaskId());
    }

    private void registry() {
        if (this.getBoolean("visuals.blizzard")) {
            this.visualsRegistry.put(new BlizzardVisual(), 10);
        }
        if (this.getBoolean("visuals.fallingSnow")) {
            this.visualsRegistry.put(new FallingSnowVisual(), 20);
        }
        if (this.getBoolean("visuals.fallingLeaves")) {
            this.visualsRegistry.put(new DefaultFallingLeafVisual(SeasonType.FALL), 40);
        }
        if (this.getBoolean("visuals.fallingLeavesSummer")) {
            this.visualsRegistry.put(new DefaultFallingLeafVisual(SeasonType.SUMMER), 40);
        }
        if (this.getBoolean("visuals.applesDropping")) {
            this.visualsRegistry.put(new ApplesDroppingVisual(), 4);
        }
        if (this.getBoolean("visuals.aurora")) {
            this.visualsRegistry.put(new AuroraVisual(), 0);
        }
    }

    @Override
    public void tick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Core.getWorldHandler().isWorldEnabled(player.getWorld().getName()) || HooksHandler.isPlayerVanished(player)) continue;
            Season season = Core.getSeasonHandler().getSeason(player.getWorld());
            this.activate(player, season.getType());
        }
    }

    private void activate(Player player, SeasonType seasonType) {
        for (Map.Entry<IVisualType, Integer> entry : this.visualsRegistry.entrySet()) {
            if (!entry.getKey().getType().equals((Object)seasonType) || !ASManager.doChancesPass(entry.getValue())) continue;
            entry.getKey().activate(player);
        }
    }
}

