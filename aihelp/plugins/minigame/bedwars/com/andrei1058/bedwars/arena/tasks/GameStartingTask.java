/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package com.andrei1058.bedwars.arena.tasks;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.NextEvent;
import com.andrei1058.bedwars.api.arena.generator.GeneratorType;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.tasks.StartingTask;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.team.BedWarsTeam;
import com.andrei1058.bedwars.arena.team.LegacyTeamAssigner;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.support.papi.SupportPAPI;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class GameStartingTask
implements Runnable,
StartingTask {
    private int countdown;
    private final IArena arena;
    private final BukkitTask task;

    public GameStartingTask(Arena arena) {
        this.arena = arena;
        this.countdown = BedWars.config.getInt("countdowns.game-start-regular");
        this.task = Bukkit.getScheduler().runTaskTimer((Plugin)BedWars.plugin, (Runnable)this, 0L, 20L);
    }

    @Override
    public int getCountdown() {
        return this.countdown;
    }

    @Override
    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    @Override
    public IArena getArena() {
        return this.arena;
    }

    @Override
    public int getTask() {
        return this.task.getTaskId();
    }

    @Override
    public BukkitTask getBukkitTask() {
        return this.task;
    }

    @Override
    public void run() {
        if (this.countdown == 0) {
            if (BedWars.config.getBoolean("use-experimental-team-assigner")) {
                this.getArena().getTeamAssigner().assignTeams(this.getArena());
            } else {
                LegacyTeamAssigner.assignTeams(this.getArena());
            }
            for (ITeam team : this.getArena().getTeams()) {
                BedWars.nms.colorBed(team);
                if (!team.getMembers().isEmpty()) continue;
                team.setBedDestroyed(true);
                if (!this.getArena().getConfig().getBoolean("disable-generator-for-empty-teams")) continue;
                for (IGenerator gen : team.getGenerators()) {
                    gen.disable();
                }
            }
            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
                for (IGenerator og : this.getArena().getOreGenerators()) {
                    if (og.getType() != GeneratorType.EMERALD && og.getType() != GeneratorType.DIAMOND) continue;
                    og.enableRotation();
                }
            }, 60L);
            this.spawnPlayers();
            BedWars.getAPI().getRestoreAdapter().onLobbyRemoval(this.arena);
            this.task.cancel();
            this.getArena().changeStatus(GameState.playing);
            if (this.getArena().getUpgradeDiamondsCount() < this.getArena().getUpgradeEmeraldsCount()) {
                this.getArena().setNextEvent(NextEvent.DIAMOND_GENERATOR_TIER_II);
            } else {
                this.getArena().setNextEvent(NextEvent.EMERALD_GENERATOR_TIER_II);
            }
            for (ITeam bwt : this.getArena().getTeams()) {
                bwt.spawnNPCs();
            }
            return;
        }
        if (this.getCountdown() % 10 == 0 || this.getCountdown() <= 5) {
            if (this.getCountdown() < 5) {
                Sounds.playSound("game-countdown-s" + this.getCountdown(), this.getArena().getPlayers());
            } else {
                Sounds.playSound("game-countdown-others", this.getArena().getPlayers());
            }
            for (Player player : this.getArena().getPlayers()) {
                Language playerLang = Language.getPlayerLanguage(player);
                String[] titleSubtitle = Language.getCountDownTitle(playerLang, this.getCountdown());
                BedWars.nms.sendTitle(player, titleSubtitle[0], titleSubtitle[1], 0, 20, 10);
                player.sendMessage(Language.getMsg(player, Messages.ARENA_STATUS_START_COUNTDOWN_CHAT).replace("{time}", String.valueOf(this.getCountdown())));
            }
        }
        --this.countdown;
    }

    private void spawnPlayers() {
        for (ITeam bwt : this.getArena().getTeams()) {
            for (Player p : new ArrayList<Player>(bwt.getMembers())) {
                BedWarsTeam.reSpawnInvulnerability.put(p.getUniqueId(), System.currentTimeMillis() + 2000L);
                bwt.firstSpawn(p);
                Sounds.playSound("game-countdown-start", p);
                BedWars.nms.sendTitle(p, Language.getMsg(p, Messages.ARENA_STATUS_START_PLAYER_TITLE), null, 0, 30, 10);
                for (String tut : Language.getList(p, Messages.ARENA_STATUS_START_PLAYER_TUTORIAL)) {
                    p.sendMessage(SupportPAPI.getSupportPAPI().replace(p, tut));
                }
            }
        }
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }
}

