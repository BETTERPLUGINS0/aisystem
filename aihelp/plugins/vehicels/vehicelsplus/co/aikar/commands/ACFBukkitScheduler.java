/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package co.aikar.commands;

import co.aikar.commands.BukkitCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class ACFBukkitScheduler {
    private int localeTask;

    public void registerSchedulerDependencies(BukkitCommandManager bukkitCommandManager) {
        bukkitCommandManager.registerDependency(BukkitScheduler.class, Bukkit.getScheduler());
    }

    public void createDelayedTask(Plugin plugin, Runnable runnable, long l) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, l);
    }

    public void createLocaleTask(Plugin plugin, Runnable runnable, long l, long l2) {
        this.localeTask = Bukkit.getScheduler().runTaskTimer(plugin, runnable, l, l2).getTaskId();
    }

    public void cancelLocaleTask() {
        Bukkit.getScheduler().cancelTask(this.localeTask);
    }
}

