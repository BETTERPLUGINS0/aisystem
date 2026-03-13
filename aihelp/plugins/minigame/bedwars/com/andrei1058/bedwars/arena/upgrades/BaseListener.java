/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.potion.PotionEffect
 */
package com.andrei1058.bedwars.arena.upgrades;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.player.PlayerBaseEnterEvent;
import com.andrei1058.bedwars.api.events.player.PlayerBaseLeaveEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.team.BedWarsTeam;
import java.util.Map;
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;

public class BaseListener
implements Listener {
    public static Map<Player, ITeam> isOnABase = new WeakHashMap<Player, ITeam>();

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent e) {
        IArena a = Arena.getArenaByIdentifier(e.getPlayer().getWorld().getName());
        if (a == null) {
            return;
        }
        if (a.getStatus() != GameState.playing) {
            return;
        }
        Player p = e.getPlayer();
        BaseListener.checkEvents(p, a);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (isOnABase.containsKey(p)) {
            IArena a = Arena.getArenaByPlayer(p);
            if (a == null) {
                isOnABase.remove(p);
                return;
            }
            BaseListener.checkEvents(p, a);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        IArena a = Arena.getArenaByPlayer(e.getEntity());
        if (a == null) {
            return;
        }
        BaseListener.checkEvents(e.getEntity(), a);
    }

    private static void checkEvents(Player p, IArena a) {
        if (p == null || a == null) {
            return;
        }
        if (a.isSpectator(p)) {
            return;
        }
        if (a.isReSpawning(p)) {
            return;
        }
        boolean notOnBase = true;
        for (ITeam bwt : a.getTeams()) {
            if (!(p.getLocation().distance(bwt.getBed()) <= (double)a.getIslandRadius())) continue;
            notOnBase = false;
            if (isOnABase.containsKey(p)) {
                if (isOnABase.get(p) == bwt) continue;
                Bukkit.getPluginManager().callEvent((Event)new PlayerBaseLeaveEvent(p, isOnABase.get(p)));
                if (!Arena.magicMilk.containsKey(p.getUniqueId())) {
                    Bukkit.getPluginManager().callEvent((Event)new PlayerBaseEnterEvent(p, bwt));
                }
                isOnABase.replace(p, bwt);
                continue;
            }
            if (Arena.magicMilk.containsKey(p.getUniqueId())) continue;
            Bukkit.getPluginManager().callEvent((Event)new PlayerBaseEnterEvent(p, bwt));
            isOnABase.put(p, bwt);
        }
        if (notOnBase && isOnABase.containsKey(p)) {
            Bukkit.getPluginManager().callEvent((Event)new PlayerBaseLeaveEvent(p, isOnABase.get(p)));
            isOnABase.remove(p);
        }
    }

    @EventHandler
    public void onBaseEnter(PlayerBaseEnterEvent e) {
        if (e == null) {
            return;
        }
        ITeam team = e.getTeam();
        if (team.isMember(e.getPlayer())) {
            for (PotionEffect ef : team.getBaseEffects()) {
                e.getPlayer().addPotionEffect(ef, true);
            }
        } else if (!team.getActiveTraps().isEmpty() && !team.isBedDestroyed()) {
            team.getActiveTraps().get(0).trigger(team, e.getPlayer());
            team.getActiveTraps().remove(0);
        }
    }

    @EventHandler
    public void onBaseLeave(PlayerBaseLeaveEvent e) {
        if (e == null) {
            return;
        }
        BedWarsTeam t = (BedWarsTeam)e.getTeam();
        if (t.isMember(e.getPlayer())) {
            for (PotionEffect pef : e.getPlayer().getActivePotionEffects()) {
                for (PotionEffect pf : t.getBaseEffects()) {
                    if (pef.getType() != pf.getType()) continue;
                    e.getPlayer().removePotionEffect(pf.getType());
                }
            }
        }
    }

    @EventHandler
    public void onArenaLeave(PlayerLeaveArenaEvent event) {
        isOnABase.remove(event.getPlayer());
    }
}

