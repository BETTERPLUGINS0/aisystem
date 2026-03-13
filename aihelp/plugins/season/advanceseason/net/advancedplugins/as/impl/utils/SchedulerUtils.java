/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.utils;

import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.FoliaScheduler;
import org.bukkit.plugin.Plugin;

public class SchedulerUtils {
    public static int runTaskLater(Runnable runnable, long l) {
        return FoliaScheduler.runTaskLater((Plugin)ASManager.getInstance(), runnable, l).getTaskId();
    }

    public static int runTaskLater(Runnable runnable) {
        return SchedulerUtils.runTaskLater(runnable, 1L);
    }

    public static int runTaskTimer(Runnable runnable, long l, long l2) {
        return FoliaScheduler.runTaskTimer((Plugin)ASManager.getInstance(), runnable, l, l2).getTaskId();
    }

    public static int runTaskTimerAsync(Runnable runnable, long l, long l2) {
        return FoliaScheduler.runTaskTimerAsynchronously((Plugin)ASManager.getInstance(), runnable, l, l2).getTaskId();
    }

    public static int runTask(Runnable runnable) {
        return FoliaScheduler.runTask((Plugin)ASManager.getInstance(), runnable).getTaskId();
    }

    public static int runTaskAsync(Runnable runnable) {
        return FoliaScheduler.runTaskAsynchronously((Plugin)ASManager.getInstance(), runnable).getTaskId();
    }
}

