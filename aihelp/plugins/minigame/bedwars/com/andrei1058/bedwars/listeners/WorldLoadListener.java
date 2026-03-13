/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.world.WorldLoadEvent
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import java.util.LinkedList;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadListener
implements Listener {
    @EventHandler
    public void onLoad(WorldLoadEvent e) {
        for (IArena a : new LinkedList<IArena>(Arena.getEnableQueue())) {
            if (!a.getWorldName().equalsIgnoreCase(e.getWorld().getName())) continue;
            a.init(e.getWorld());
            return;
        }
    }
}

