/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.andrei1058.bedwars.lobbysocket;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.lobbysocket.ArenaSocket;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SendTask {
    public SendTask() {
        Bukkit.getScheduler().runTaskTimer((Plugin)BedWars.plugin, () -> {
            final ArrayList<IArena> arenas = new ArrayList<IArena>(Arena.getArenas());
            new BukkitRunnable(){

                public void run() {
                    for (IArena a : arenas) {
                        ArenaSocket.sendMessage(ArenaSocket.formatUpdateMessage(a));
                    }
                }
            }.runTaskAsynchronously((Plugin)BedWars.plugin);
        }, 100L, 30L);
    }
}

