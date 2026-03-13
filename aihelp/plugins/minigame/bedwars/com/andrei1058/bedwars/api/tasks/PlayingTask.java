/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.scheduler.BukkitTask
 */
package com.andrei1058.bedwars.api.tasks;

import com.andrei1058.bedwars.api.arena.IArena;
import org.bukkit.scheduler.BukkitTask;

public interface PlayingTask {
    public IArena getArena();

    public BukkitTask getBukkitTask();

    public int getTask();

    public int getBedsDestroyCountdown();

    public int getDragonSpawnCountdown();

    public int getGameEndCountdown();

    public void cancel();
}

