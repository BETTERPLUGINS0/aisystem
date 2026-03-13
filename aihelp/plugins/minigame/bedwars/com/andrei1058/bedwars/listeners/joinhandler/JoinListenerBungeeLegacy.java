/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 */
package com.andrei1058.bedwars.listeners.joinhandler;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.ReJoin;
import com.andrei1058.bedwars.configuration.Permissions;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.listeners.joinhandler.JoinHandlerCommon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinListenerBungeeLegacy
implements Listener {
    @EventHandler(priority=EventPriority.HIGH)
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if (Arena.getArenas().isEmpty() && !Arena.getEnableQueue().isEmpty()) {
            e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, Language.getMsg(e.getPlayer(), Messages.ARENA_STATUS_RESTARTING_NAME));
            return;
        }
        ReJoin reJoin = ReJoin.getPlayer(p);
        if (reJoin != null) {
            if (!p.hasPermission(Permissions.PERMISSION_REJOIN) && !reJoin.canReJoin()) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Language.getDefaultLanguage().m(Messages.REJOIN_DENIED));
                reJoin.destroy(true);
            }
            return;
        }
        IArena arena = Arena.getArenas().get(0);
        if (arena != null) {
            if (arena.getStatus() == GameState.waiting || arena.getStatus() == GameState.starting && arena.getStartingTask().getCountdown() > 1) {
                if (arena.getPlayers().size() >= arena.getMaxPlayers()) {
                    if (Arena.isVip(p)) {
                        boolean canJoin = false;
                        for (Player inGame : arena.getPlayers()) {
                            if (Arena.isVip(inGame)) continue;
                            canJoin = true;
                            inGame.kickPlayer(Language.getMsg(inGame, Messages.ARENA_JOIN_VIP_KICK));
                            break;
                        }
                        if (!canJoin) {
                            e.disallow(PlayerLoginEvent.Result.KICK_FULL, Language.getDefaultLanguage().m(Messages.COMMAND_JOIN_DENIED_IS_FULL_OF_VIPS));
                        }
                    } else {
                        e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Language.getMsg(e.getPlayer(), Messages.COMMAND_JOIN_DENIED_IS_FULL));
                    }
                }
            } else if (arena.getStatus() == GameState.playing) {
                if (!arena.isAllowSpectate()) {
                    e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Language.getDefaultLanguage().m(Messages.COMMAND_JOIN_SPECTATOR_DENIED_MSG));
                }
            } else {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Language.getDefaultLanguage().m(Messages.ARENA_STATUS_RESTARTING_NAME));
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        if (Arena.getArenas().isEmpty() && !Arena.getEnableQueue().isEmpty()) {
            p.kickPlayer(Language.getMsg(e.getPlayer(), Messages.ARENA_STATUS_RESTARTING_NAME));
            return;
        }
        JoinHandlerCommon.displayCustomerDetails(p);
        if (Arena.getArenas().isEmpty()) {
            if (p.hasPermission("bw.setup")) {
                p.performCommand(BedWars.mainCmd);
            }
        } else {
            IArena arena = Arena.getArenas().get(0);
            if (arena.getStatus() == GameState.waiting || arena.getStatus() == GameState.starting) {
                if (arena.addPlayer(p, false)) {
                    Sounds.playSound("join-allowed", p);
                } else {
                    p.kickPlayer(Language.getMsg(p, Messages.COMMAND_JOIN_DENIED_IS_FULL));
                }
            } else {
                ReJoin reJoin = ReJoin.getPlayer(p);
                if (reJoin != null) {
                    if (reJoin.canReJoin()) {
                        reJoin.reJoin(p);
                        reJoin.destroy(false);
                        return;
                    }
                    p.sendMessage(Language.getMsg(p, Messages.REJOIN_DENIED));
                    reJoin.destroy(true);
                }
                if (arena.addSpectator(p, false, null)) {
                    Sounds.playSound("spectate-allowed", p);
                } else {
                    p.kickPlayer(Language.getMsg(p, Messages.COMMAND_JOIN_SPECTATOR_DENIED_MSG));
                }
            }
        }
    }
}

