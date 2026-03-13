/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.boss.BarColor
 *  org.bukkit.boss.BarFlag
 *  org.bukkit.boss.BarStyle
 *  org.bukkit.boss.BossBar
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.magmacore.initialization;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginInitializationProgressBar {
    private static final Map<String, ProgressBarData> progressBars = new ConcurrentHashMap<String, ProgressBarData>();

    private PluginInitializationProgressBar() {
    }

    public static void start(JavaPlugin plugin, String displayName, String permission, int totalSteps) {
        PluginInitializationProgressBar.complete(plugin);
        ProgressBarData progressBarData = new ProgressBarData(plugin, displayName, permission, totalSteps);
        progressBars.put(plugin.getName(), progressBarData);
        progressBarData.start();
    }

    public static void step(JavaPlugin plugin, String description) {
        ProgressBarData progressBarData = progressBars.get(plugin.getName());
        if (progressBarData == null) {
            return;
        }
        progressBarData.step(description);
    }

    public static void status(JavaPlugin plugin, String description) {
        ProgressBarData progressBarData = progressBars.get(plugin.getName());
        if (progressBarData == null) {
            return;
        }
        progressBarData.status(description);
    }

    public static void complete(JavaPlugin plugin) {
        ProgressBarData progressBarData = progressBars.remove(plugin.getName());
        if (progressBarData == null) {
            return;
        }
        progressBarData.complete();
    }

    private static class ProgressBarData {
        private final JavaPlugin plugin;
        private final String displayName;
        private final String permission;
        private final int totalSteps;
        private int currentStep;
        private BossBar bossBar;
        private Listener listener;

        private ProgressBarData(JavaPlugin plugin, String displayName, String permission, int totalSteps) {
            this.plugin = plugin;
            this.displayName = displayName;
            this.permission = permission;
            this.totalSteps = totalSteps;
        }

        private void start() {
            this.currentStep = 0;
            this.bossBar = Bukkit.createBossBar((String)(String.valueOf(ChatColor.GREEN) + String.valueOf(ChatColor.BOLD) + this.displayName + String.valueOf(ChatColor.WHITE) + " \u25b8 Initializing..."), (BarColor)BarColor.GREEN, (BarStyle)BarStyle.SEGMENTED_10, (BarFlag[])new BarFlag[0]);
            this.bossBar.setProgress(0.0);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!this.canView(player)) continue;
                this.bossBar.addPlayer(player);
            }
            this.listener = new Listener(){

                @EventHandler
                public void onPlayerJoin(PlayerJoinEvent event) {
                    if (bossBar != null && this.canView(event.getPlayer())) {
                        bossBar.addPlayer(event.getPlayer());
                    }
                }
            };
            Bukkit.getPluginManager().registerEvents(this.listener, (Plugin)this.plugin);
        }

        private void step(String description) {
            ++this.currentStep;
            double progress = this.totalSteps <= 0 ? 1.0 : Math.min((double)this.currentStep / (double)this.totalSteps, 1.0);
            this.update(description, progress);
        }

        private void status(String description) {
            double progress = this.totalSteps <= 0 ? 0.0 : Math.min((double)this.currentStep / (double)this.totalSteps, 1.0);
            this.update(description, progress);
        }

        private void update(String description, double progress) {
            Runnable task = () -> {
                if (this.bossBar == null) {
                    return;
                }
                this.bossBar.setTitle(String.valueOf(ChatColor.GREEN) + String.valueOf(ChatColor.BOLD) + this.displayName + String.valueOf(ChatColor.WHITE) + " \u25b8 " + String.valueOf(ChatColor.YELLOW) + description);
                this.bossBar.setProgress(progress);
            };
            if (Bukkit.isPrimaryThread()) {
                task.run();
            } else {
                Bukkit.getScheduler().runTask((Plugin)this.plugin, task);
            }
        }

        private void complete() {
            if (this.bossBar != null) {
                this.bossBar.removeAll();
                this.bossBar = null;
            }
            if (this.listener != null) {
                HandlerList.unregisterAll((Listener)this.listener);
                this.listener = null;
            }
        }

        private boolean canView(Player player) {
            if (this.permission == null || this.permission.isBlank()) {
                return player.isOp();
            }
            return player.hasPermission(this.permission);
        }
    }
}

