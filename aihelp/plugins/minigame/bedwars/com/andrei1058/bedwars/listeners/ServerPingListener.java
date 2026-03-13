/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.server.ServerListPingEvent
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.arena.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingListener
implements Listener {
    @EventHandler
    public void onPing(ServerListPingEvent e) {
        IArena a;
        if (!Arena.getArenas().isEmpty() && (a = Arena.getArenas().get(0)) != null) {
            e.setMaxPlayers(a.getMaxPlayers());
            e.setMotd(a.getDisplayStatus(Language.getDefaultLanguage()));
        }
    }
}

