/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.levels.internal;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.player.PlayerBedBreakEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.andrei1058.bedwars.api.events.player.PlayerXpGainEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.configuration.LevelsConfig;
import com.andrei1058.bedwars.levels.internal.PlayerLevel;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class LevelListeners
implements Listener {
    public static LevelListeners instance;

    public LevelListeners() {
        instance = this;
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e) {
        UUID u = e.getPlayer().getUniqueId();
        new PlayerLevel(u, 1, 0);
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> {
            Object[] levelData = BedWars.getRemoteDatabase().getLevelData(u);
            PlayerLevel.getLevelByPlayer(u).lazyLoad((Integer)levelData[0], (Integer)levelData[1]);
        });
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUID u = e.getPlayer().getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> {
            PlayerLevel pl = PlayerLevel.getLevelByPlayer(u);
            pl.destroy();
        });
    }

    @EventHandler
    public void onGameEnd(GameEndEvent e) {
        Player p1;
        for (UUID p : e.getWinners()) {
            int xpAmountPerTmt;
            ITeam bwt;
            if (PlayerLevel.getLevelByPlayer(p) == null || (p1 = Bukkit.getPlayer((UUID)p)) == null) continue;
            int xpAmount = LevelsConfig.levels.getInt("xp-rewards.game-win");
            if (xpAmount > 0) {
                PlayerLevel.getLevelByPlayer(p).addXp(xpAmount, PlayerXpGainEvent.XpSource.GAME_WIN);
                p1.sendMessage(Language.getMsg(p1, Messages.XP_REWARD_WIN).replace("{xp}", String.valueOf(xpAmount)));
            }
            if ((bwt = e.getArena().getExTeam(p1.getUniqueId())) == null || bwt.getMembersCache().size() <= 1 || (xpAmountPerTmt = LevelsConfig.levels.getInt("xp-rewards.per-teammate")) <= 0) continue;
            int tr = xpAmountPerTmt * bwt.getMembersCache().size();
            PlayerLevel.getLevelByPlayer(p).addXp(tr, PlayerXpGainEvent.XpSource.PER_TEAMMATE);
            p1.sendMessage(Language.getMsg(p1, "xp-reward-per-teammate").replace("{xp}", String.valueOf(tr)));
        }
        for (UUID p : e.getLosers()) {
            int xpAmountPerTmt;
            ITeam bwt;
            if (PlayerLevel.getLevelByPlayer(p) == null || (p1 = Bukkit.getPlayer((UUID)p)) == null || (bwt = e.getArena().getExTeam(p1.getUniqueId())) == null || bwt.getMembersCache().size() <= 1 || (xpAmountPerTmt = LevelsConfig.levels.getInt("xp-rewards.per-teammate")) <= 0) continue;
            int tr = LevelsConfig.levels.getInt("xp-rewards.per-teammate") * bwt.getMembersCache().size();
            PlayerLevel.getLevelByPlayer(p).addXp(tr, PlayerXpGainEvent.XpSource.PER_TEAMMATE);
            p1.sendMessage(Language.getMsg(p1, Messages.XP_REWARD_PER_TEAMMATE).replace("{xp}", String.valueOf(tr)));
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onArenaLeave(PlayerLeaveArenaEvent e) {
        UUID u = e.getPlayer().getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> {
            PlayerLevel pl = PlayerLevel.getLevelByPlayer(u);
            if (pl != null) {
                pl.updateDatabase();
            }
        });
    }

    @EventHandler
    public void onBreakBed(PlayerBedBreakEvent e) {
        Player player = e.getPlayer();
        if (player == null) {
            return;
        }
        int bedDestroy = LevelsConfig.levels.getInt("xp-rewards.bed-destroyed");
        if (bedDestroy > 0) {
            PlayerLevel.getLevelByPlayer(player.getUniqueId()).addXp(bedDestroy, PlayerXpGainEvent.XpSource.BED_DESTROYED);
            player.sendMessage(Language.getMsg(player, Messages.XP_REWARD_BED_DESTROY).replace("{xp}", String.valueOf(bedDestroy)));
        }
    }

    @EventHandler
    public void onKill(PlayerKillEvent e) {
        Player player = e.getKiller();
        Player victim = e.getVictim();
        if (player == null || victim.equals((Object)player)) {
            return;
        }
        int finalKill = LevelsConfig.levels.getInt("xp-rewards.final-kill");
        int regularKill = LevelsConfig.levels.getInt("xp-rewards.regular-kill");
        if (e.getCause().isFinalKill()) {
            if (finalKill > 0) {
                PlayerLevel.getLevelByPlayer(player.getUniqueId()).addXp(finalKill, PlayerXpGainEvent.XpSource.FINAL_KILL);
                player.sendMessage(Language.getMsg(player, Messages.XP_REWARD_FINAL_KILL).replace("{xp}", String.valueOf(finalKill)));
            }
        } else if (regularKill > 0) {
            PlayerLevel.getLevelByPlayer(player.getUniqueId()).addXp(regularKill, PlayerXpGainEvent.XpSource.REGULAR_KILL);
            player.sendMessage(Language.getMsg(player, Messages.XP_REWARD_REGULAR_KILL).replace("{xp}", String.valueOf(regularKill)));
        }
    }
}

