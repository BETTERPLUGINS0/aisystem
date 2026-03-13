/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.Sign
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 */
package com.andrei1058.bedwars.listeners.blockstatus;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.api.events.server.ArenaEnableEvent;
import com.andrei1058.bedwars.arena.Arena;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockStatusListener
implements Listener {
    @EventHandler
    public void onArenaEnable(ArenaEnableEvent e) {
        if (e == null) {
            return;
        }
        BlockStatusListener.updateBlock((Arena)e.getArena());
    }

    @EventHandler
    public void onStatusChange(GameStateChangeEvent e) {
        if (e == null) {
            return;
        }
        BlockStatusListener.updateBlock((Arena)e.getArena());
    }

    public static void updateBlock(Arena a) {
        if (a == null) {
            return;
        }
        for (Block s : a.getSigns()) {
            if (!(s.getState() instanceof Sign)) continue;
            String path = "";
            String data = "";
            switch (a.getStatus()) {
                case waiting: {
                    path = "status-block.waiting.material";
                    data = "status-block.waiting.data";
                    break;
                }
                case playing: {
                    path = "status-block.playing.material";
                    data = "status-block.starting.data";
                    break;
                }
                case starting: {
                    path = "status-block.playing.material";
                    data = "status-block.playing.data";
                    break;
                }
                case restarting: {
                    path = "status-block.restarting.material";
                    data = "status-block.restarting.data";
                    break;
                }
                default: {
                    throw new IllegalStateException("Unhandled game status!");
                }
            }
            BedWars.nms.setJoinSignBackground(s.getState(), Material.valueOf((String)BedWars.signs.getString(path)));
            BedWars.nms.setJoinSignBackgroundBlockData(s.getState(), (byte)BedWars.signs.getInt(data));
        }
    }
}

