/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
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
import com.andrei1058.bedwars.lobbysocket.LoadedUser;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import com.andrei1058.bedwars.support.preloadedparty.PreLoadedParty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class JoinListenerBungee
implements Listener {
    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        LoadedUser proxyUser = LoadedUser.getPreLoaded(p.getUniqueId());
        if (proxyUser == null) {
            if (!e.getPlayer().hasPermission("bw.setup")) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Language.getMsg(p, Messages.ARENA_JOIN_DENIED_NO_PROXY));
            }
        } else {
            GameState status;
            Language playerLang = proxyUser.getLanguage() == null ? Language.getDefaultLanguage() : proxyUser.getLanguage();
            ReJoin reJoin = ReJoin.getPlayer(p);
            if (reJoin != null) {
                if (!p.hasPermission(Permissions.PERMISSION_REJOIN) && !reJoin.canReJoin()) {
                    e.disallow(PlayerLoginEvent.Result.KICK_OTHER, playerLang.m(Messages.REJOIN_DENIED));
                    reJoin.destroy(true);
                }
                return;
            }
            IArena arena = Arena.getArenaByIdentifier(proxyUser.getArenaIdentifier());
            GameState gameState = status = arena != null ? arena.getStatus() : null;
            if (arena == null || proxyUser.isTimedOut() || status == GameState.restarting) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, playerLang.m(Messages.ARENA_STATUS_RESTARTING_NAME));
                proxyUser.destroy("Time out or game unavailable at PlayerLoginEvent");
                return;
            }
            switch (status) {
                case starting: 
                case waiting: {
                    if (arena.getPlayers().size() < arena.getMaxPlayers() || !Arena.isVip(p)) break;
                    boolean canJoin = false;
                    for (Player inGame : arena.getPlayers()) {
                        if (Arena.isVip(inGame)) continue;
                        canJoin = true;
                        inGame.kickPlayer(Language.getMsg(inGame, Messages.ARENA_JOIN_VIP_KICK));
                        break;
                    }
                    if (canJoin) break;
                    e.disallow(PlayerLoginEvent.Result.KICK_FULL, playerLang.m(Messages.COMMAND_JOIN_DENIED_IS_FULL_OF_VIPS));
                    break;
                }
                case playing: {
                    if (arena.isAllowSpectate()) break;
                    e.disallow(PlayerLoginEvent.Result.KICK_OTHER, playerLang.m(Messages.COMMAND_JOIN_SPECTATOR_DENIED_MSG));
                    break;
                }
                default: {
                    throw new IllegalStateException("Unhandled game status!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer() == null) {
            return;
        }
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        LoadedUser proxyUser = LoadedUser.getPreLoaded(p.getUniqueId());
        if (proxyUser == null) {
            if (p.hasPermission("bw.setup")) {
                JoinHandlerCommon.displayCustomerDetails(p);
                Bukkit.dispatchCommand((CommandSender)p, (String)"bw");
                World mainWorld = (World)Bukkit.getWorlds().get(0);
                if (mainWorld != null) {
                    TeleportManager.teleportC((Entity)p, mainWorld.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                }
                for (Player inGame : Bukkit.getOnlinePlayers()) {
                    if (inGame.equals((Object)p) || !Arena.isInArena(inGame)) continue;
                    BedWars.nms.spigotHidePlayer(p, inGame);
                    BedWars.nms.spigotHidePlayer(inGame, p);
                }
            } else {
                p.kickPlayer(Language.getMsg(p, Messages.ARENA_JOIN_DENIED_NO_PROXY));
            }
        } else {
            GameState status;
            Language playerLang = proxyUser.getLanguage() == null ? Language.getDefaultLanguage() : proxyUser.getLanguage();
            ReJoin reJoin = ReJoin.getPlayer(p);
            if (reJoin != null) {
                if (reJoin.canReJoin()) {
                    JoinHandlerCommon.displayCustomerDetails(p);
                    reJoin.reJoin(p);
                    Language.setPlayerLanguage(p.getUniqueId(), playerLang.getIso());
                } else {
                    p.kickPlayer(playerLang.m(Messages.REJOIN_DENIED));
                }
                proxyUser.destroy("Rejoin handled. PreLoaded user no longer needed.");
                return;
            }
            IArena arena = Arena.getArenaByIdentifier(proxyUser.getArenaIdentifier());
            GameState gameState = status = arena != null ? arena.getStatus() : null;
            if (arena == null || proxyUser.isTimedOut() || status == GameState.restarting) {
                p.kickPlayer(playerLang.m(Messages.ARENA_STATUS_RESTARTING_NAME));
                proxyUser.destroy("Time out or game unavailable at PlayerLoginEvent");
                return;
            }
            Language.setPlayerLanguage(p.getUniqueId(), playerLang.getIso());
            JoinHandlerCommon.displayCustomerDetails(p);
            switch (status) {
                case starting: 
                case waiting: {
                    Sounds.playSound("join-allowed", p);
                    if (proxyUser.getPartyOwnerOrSpectateTarget() == null) {
                        if (arena.addPlayer(p, true)) break;
                        p.kickPlayer(Language.getMsg(p, Messages.ARENA_JOIN_DENIED_NO_PROXY));
                        break;
                    }
                    Player partyOwner = Bukkit.getPlayer((String)proxyUser.getPartyOwnerOrSpectateTarget());
                    if (partyOwner != null && partyOwner.isOnline()) {
                        if (partyOwner.equals((Object)p)) {
                            BedWars.getParty().createParty(p, new Player[0]);
                            PreLoadedParty preLoadedParty = PreLoadedParty.getPartyByOwner(partyOwner.getName());
                            if (preLoadedParty != null) {
                                preLoadedParty.teamUp();
                            }
                        } else {
                            BedWars.getParty().addMember(partyOwner, p);
                        }
                    } else {
                        PreLoadedParty preLoadedParty = PreLoadedParty.getPartyByOwner(proxyUser.getPartyOwnerOrSpectateTarget());
                        if (preLoadedParty == null) {
                            preLoadedParty = new PreLoadedParty(proxyUser.getPartyOwnerOrSpectateTarget());
                        }
                        preLoadedParty.addMember(p);
                    }
                    if (arena.addPlayer(p, true)) break;
                    p.kickPlayer(Language.getMsg(p, Messages.ARENA_JOIN_DENIED_NO_PROXY));
                    break;
                }
                case playing: {
                    Player targetPlayer;
                    Sounds.playSound("spectate-allowed", p);
                    Location spectatorTarget = null;
                    if (proxyUser.getPartyOwnerOrSpectateTarget() != null && (targetPlayer = Bukkit.getPlayer((String)proxyUser.getPartyOwnerOrSpectateTarget())) != null) {
                        spectatorTarget = targetPlayer.getLocation();
                    }
                    arena.addSpectator(p, false, spectatorTarget);
                    break;
                }
                default: {
                    throw new IllegalStateException("Unhandled game status!");
                }
            }
            proxyUser.destroy("Joined as player or spectator. PreLoaded user no longer needed.");
        }
    }
}

