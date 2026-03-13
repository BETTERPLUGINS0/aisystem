/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.papermc.paper.threadedregions.scheduler.ScheduledTask
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package net.advancedplugins.as.impl.utils;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class FoliaScheduler {
    public static final boolean isFolia = FoliaScheduler.checkFolia();

    private static boolean checkFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException classNotFoundException) {
            return false;
        }
    }

    public static Task runTask(Plugin plugin, Runnable runnable) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().execute(plugin, runnable);
            return new Task(null);
        }
        return new Task(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    public static Task runTaskLater(Plugin plugin, Runnable runnable, long l) {
        if (isFolia) {
            return new Task(Bukkit.getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> runnable.run(), l < 1L ? 1L : l));
        }
        return new Task(Bukkit.getScheduler().runTaskLater(plugin, runnable, l));
    }

    public static Task runTaskTimer(Plugin plugin, Runnable runnable, long l, long l2) {
        if (isFolia) {
            return new Task(Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> runnable.run(), l < 1L ? 1L : l, l2));
        }
        return new Task(Bukkit.getScheduler().runTaskTimer(plugin, runnable, l, l2));
    }

    public static Task runTaskAsynchronously(Plugin plugin, Runnable runnable) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().execute(plugin, runnable);
            return new Task(null);
        }
        return new Task(Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
    }

    public static Task runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long l) {
        if (isFolia) {
            return new Task(Bukkit.getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> runnable.run(), l < 1L ? 1L : l));
        }
        return new Task(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, l));
    }

    public static Task runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long l, long l2) {
        if (isFolia) {
            return new Task(Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> runnable.run(), l < 1L ? 1L : l, l2));
        }
        return new Task(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, l, l2));
    }

    public static void runTaskTimerAsynchronously(Plugin plugin, Consumer<Task> consumer, long l, long l2) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> consumer.accept(new Task(scheduledTask)), l < 1L ? 1L : l, l2);
        } else {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, (T bukkitTask) -> consumer.accept(new Task((BukkitTask)bukkitTask)), l, l2);
        }
    }

    public static boolean isFolia() {
        return isFolia;
    }

    public static void cancelAll(Plugin plugin) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
        } else {
            Bukkit.getScheduler().cancelTasks(plugin);
        }
    }

    public static class Task {
        private Object foliaTask;
        private BukkitTask bukkitTask;

        public Task(Object object) {
            this.foliaTask = object;
        }

        public Task(BukkitTask bukkitTask) {
            this.bukkitTask = bukkitTask;
        }

        public void cancel() {
            if (this.foliaTask == null && this.bukkitTask == null) {
                return;
            }
            if (this.foliaTask != null) {
                ((ScheduledTask)this.foliaTask).cancel();
            } else {
                this.bukkitTask.cancel();
            }
        }

        public int getTaskId() {
            if (this.foliaTask == null && this.bukkitTask == null) {
                return -1;
            }
            if (this.foliaTask != null) {
                return -1;
            }
            return this.bukkitTask.getTaskId();
        }
    }
}

