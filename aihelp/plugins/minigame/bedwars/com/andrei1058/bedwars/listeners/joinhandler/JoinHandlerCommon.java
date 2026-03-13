/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerPreLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.listeners.joinhandler;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

public class JoinHandlerCommon
implements Listener {
    protected static void displayCustomerDetails(Player player) {
        if (player == null) {
            return;
        }
        if (player.getName().equalsIgnoreCase("andrei1058") || player.getName().equalsIgnoreCase("andreea1058") || player.getName().equalsIgnoreCase("Dani3l_FTW")) {
            player.sendMessage("\u00a78[\u00a7f" + BedWars.plugin.getName() + " v" + BedWars.plugin.getDescription().getVersion() + "\u00a78]\u00a77\u00a7m---------------------------");
            player.sendMessage("");
            player.sendMessage("\u00a77User ID: \u00a7f%%__USER__%%");
            player.sendMessage("\u00a77Download ID: \u00a7f%%__NONCE__%%");
            player.sendMessage("");
            player.sendMessage("\u00a78[\u00a7f" + BedWars.plugin.getName() + "\u00a78]\u00a77\u00a7m---------------------------");
        }
    }

    @EventHandler
    public void requestLanguage(AsyncPlayerPreLoginEvent e) {
        String iso = BedWars.getRemoteDatabase().getLanguage(e.getUniqueId());
        Bukkit.getScheduler().runTask((Plugin)BedWars.plugin, () -> Language.setPlayerLanguage(e.getUniqueId(), iso));
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void removeLanguage(PlayerLoginEvent e) {
        if (e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            Language.setPlayerLanguage(e.getPlayer().getUniqueId(), null);
        }
    }
}

