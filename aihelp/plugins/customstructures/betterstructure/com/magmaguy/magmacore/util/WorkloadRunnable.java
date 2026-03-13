/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.magmaguy.magmacore.util;

import com.magmaguy.magmacore.MagmaCore;
import com.magmaguy.magmacore.util.Workload;
import java.util.ArrayDeque;
import java.util.Deque;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WorkloadRunnable
extends BukkitRunnable {
    private final Deque<Workload> workloadDeque = new ArrayDeque<Workload>();
    private final long maxMillisPerTick;
    private final Runnable onComplete;

    public WorkloadRunnable(double percentageOfTick, Runnable onComplete) {
        this.maxMillisPerTick = Math.max((long)(0.05 * percentageOfTick * 1000.0), 2L);
        this.onComplete = onComplete;
    }

    public void addWorkload(Workload workload) {
        this.workloadDeque.add(workload);
    }

    public void startSync() {
        this.runTaskTimer((Plugin)MagmaCore.getInstance().getRequestingPlugin(), 0L, 1L);
    }

    public void startAsync() {
        this.runTaskTimerAsynchronously((Plugin)MagmaCore.getInstance().getRequestingPlugin(), 0L, 1L);
    }

    public void run() {
        long stopTime = System.currentTimeMillis() + this.maxMillisPerTick;
        while (!this.workloadDeque.isEmpty() && System.currentTimeMillis() < stopTime) {
            Workload workload = this.workloadDeque.poll();
            if (workload == null) continue;
            workload.compute();
        }
        if (this.workloadDeque.isEmpty()) {
            if (this.onComplete != null) {
                this.onComplete.run();
            }
            this.cancel();
        }
    }
}

