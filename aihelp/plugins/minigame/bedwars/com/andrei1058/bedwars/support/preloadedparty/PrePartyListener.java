/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 */
package com.andrei1058.bedwars.support.preloadedparty;

import com.andrei1058.bedwars.api.events.server.ArenaDisableEvent;
import com.andrei1058.bedwars.api.events.server.ArenaRestartEvent;
import com.andrei1058.bedwars.support.preloadedparty.PreLoadedParty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Deprecated(forRemoval=true, since="23.1.1")
public class PrePartyListener
implements Listener {
    @EventHandler
    public void onDisable(ArenaDisableEvent e) {
        PreLoadedParty plp = PreLoadedParty.getPartyByOwner(e.getWorldName());
        if (plp != null) {
            PreLoadedParty.getPreLoadedParties().remove(plp);
        }
    }

    @EventHandler
    public void onRestart(ArenaRestartEvent e) {
        PreLoadedParty plp = PreLoadedParty.getPartyByOwner(e.getWorldName());
        if (plp != null) {
            PreLoadedParty.getPreLoadedParties().remove(plp);
        }
    }
}

