/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.arena.tasks;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ReJoinTask
implements Runnable {
    private static final List<ReJoinTask> reJoinTasks = new ArrayList<ReJoinTask>();
    private final IArena arena;
    private final ITeam bedWarsTeam;
    private final BukkitTask task;

    public ReJoinTask(IArena arena, ITeam bedWarsTeam) {
        this.arena = arena;
        this.bedWarsTeam = bedWarsTeam;
        this.task = Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, (Runnable)this, (long)BedWars.config.getInt("rejoin-time") * 20L);
    }

    @Override
    public void run() {
        if (this.arena == null) {
            this.destroy();
            return;
        }
        if (this.bedWarsTeam == null) {
            this.destroy();
            return;
        }
        if (this.bedWarsTeam.getMembers() == null) {
            this.destroy();
            return;
        }
        if (this.bedWarsTeam.getMembers().isEmpty()) {
            this.bedWarsTeam.setBedDestroyed(true);
            this.destroy();
        }
    }

    public IArena getArena() {
        return this.arena;
    }

    public void destroy() {
        reJoinTasks.remove(this);
        this.task.cancel();
    }

    @NotNull
    @Contract(pure=true)
    public static Collection<ReJoinTask> getReJoinTasks() {
        return Collections.unmodifiableCollection(reJoinTasks);
    }

    public void cancel() {
        this.task.cancel();
    }
}

