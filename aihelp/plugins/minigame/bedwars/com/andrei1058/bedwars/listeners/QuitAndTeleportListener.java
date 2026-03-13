/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.LastHit;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.arena.team.BedWarsTeam;
import com.andrei1058.bedwars.commands.bedwars.subcmds.regular.CmdStats;
import com.andrei1058.bedwars.sidebar.SidebarService;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class QuitAndTeleportListener
implements Listener {
    @EventHandler
    public void onLeave(@NotNull PlayerQuitEvent e) {
        SetupSession ss;
        Player p = e.getPlayer();
        IArena a = Arena.getArenaByPlayer(p);
        if (a != null) {
            if (a.isPlayer(p)) {
                a.removePlayer(p, true);
            } else if (a.isSpectator(p)) {
                a.removeSpectator(p, true);
            }
        }
        if (Language.getLangByPlayer().containsKey(p.getUniqueId())) {
            UUID u = p.getUniqueId();
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> {
                String iso = Language.getLangByPlayer().get(p.getUniqueId()).getIso();
                if (Language.isLanguageExist(iso)) {
                    if (BedWars.config.getYml().getStringList("disabled-languages").contains(iso)) {
                        iso = Language.getDefaultLanguage().getIso();
                    }
                    BedWars.getRemoteDatabase().setLanguage(u, iso);
                }
                Language.getLangByPlayer().remove(p.getUniqueId());
            });
        }
        if (BedWars.getServerType() != ServerType.SHARED) {
            e.setQuitMessage(null);
        }
        if (BedWars.getParty().isInternal() && BedWars.getParty().hasParty(p)) {
            BedWars.getParty().removeFromParty(p);
        }
        if ((ss = SetupSession.getSession(p.getUniqueId())) != null) {
            ss.cancel();
        }
        SidebarService.getInstance().remove(e.getPlayer());
        BedWarsTeam.reSpawnInvulnerability.remove(e.getPlayer().getUniqueId());
        LastHit lh = LastHit.getLastHit(p);
        if (lh != null) {
            lh.remove();
        }
        CmdStats.getStatsCoolDown().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onWorldChange(@NotNull PlayerChangedWorldEvent e) {
        IArena arena = Arena.getArenaByPlayer(e.getPlayer());
        if (null == arena) {
            return;
        }
        if (e.getPlayer().getWorld().getName().equals(arena.getWorldName())) {
            return;
        }
        if (arena.isPlayer(e.getPlayer())) {
            arena.removePlayer(e.getPlayer(), false);
        }
        if (arena.isSpectator(e.getPlayer())) {
            arena.removeSpectator(e.getPlayer(), false);
        }
    }
}

