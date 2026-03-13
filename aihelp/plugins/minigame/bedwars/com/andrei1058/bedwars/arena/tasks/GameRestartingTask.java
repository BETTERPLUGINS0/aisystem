/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitTask
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.arena.tasks;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.shop.ShopHolo;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.api.tasks.RestartingTask;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class GameRestartingTask
implements Runnable,
RestartingTask {
    private Arena arena;
    private int restarting = BedWars.config.getInt("countdowns.game-restart") + 5;
    private final BukkitTask task;

    public GameRestartingTask(@NotNull Arena arena) {
        this.arena = arena;
        this.task = Bukkit.getScheduler().runTaskTimer((Plugin)BedWars.plugin, (Runnable)this, 0L, 20L);
        Sounds.playSound("game-end", arena.getPlayers());
        Sounds.playSound("game-end", arena.getSpectators());
        if (arena.getConfig().getGameOverridableBoolean("game-end.teleport-eliminated").booleanValue() && !arena.getPlayers().isEmpty()) {
            Random r = new Random();
            for (Player spectator : arena.getSpectators()) {
                Player target = arena.getPlayers().get(r.nextInt(arena.getPlayers().size()));
                Location loc = target.getLocation().clone();
                loc.setDirection(target.getLocation().getDirection().multiply(-1));
                loc.add(0.0, 2.0, 0.0);
                TeleportManager.teleportC((Entity)spectator, loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
        if (arena.getConfig().getGameOverridableBoolean("game-end.show-eliminated").booleanValue()) {
            for (Player spectator : arena.getSpectators()) {
                ITeam exTeam = arena.getExTeam(spectator.getUniqueId());
                if (null == exTeam) continue;
                spectator.removePotionEffect(PotionEffectType.INVISIBILITY);
                for (Player player : arena.getPlayers()) {
                    BedWars.nms.spigotShowPlayer(player, spectator);
                    BedWars.nms.spigotShowPlayer(spectator, player);
                }
            }
        }
    }

    @Override
    public int getTask() {
        return this.task.getTaskId();
    }

    @Override
    public int getRestarting() {
        return this.restarting;
    }

    @Override
    public Arena getArena() {
        return this.arena;
    }

    @Override
    public BukkitTask getBukkitTask() {
        return this.task;
    }

    @Override
    public void run() {
        --this.restarting;
        if (this.getArena().getPlayers().isEmpty() && this.restarting > 9) {
            this.restarting = 9;
        }
        if (this.restarting == 7) {
            for (Player on : new ArrayList<Player>(this.getArena().getPlayers())) {
                this.getArena().removePlayer(on, BedWars.getServerType() == ServerType.BUNGEE);
            }
            for (Player on : new ArrayList<Player>(this.getArena().getSpectators())) {
                this.getArena().removeSpectator(on, BedWars.getServerType() == ServerType.BUNGEE);
            }
        } else if (this.restarting == 4) {
            ShopHolo.clearForArena(this.getArena());
            for (Entity e : this.getArena().getWorld().getEntities()) {
                if (e.getType() != EntityType.PLAYER) continue;
                Player p = (Player)e;
                Misc.moveToLobbyOrKick(p, this.getArena(), true);
                if (this.getArena().isSpectator(p)) {
                    this.getArena().removeSpectator(p, false);
                }
                if (!this.getArena().isPlayer(p)) continue;
                this.getArena().removePlayer(p, false);
            }
            for (IGenerator eg : this.getArena().getOreGenerators()) {
                eg.disable();
            }
            for (ITeam t : this.getArena().getTeams()) {
                for (IGenerator eg : t.getGenerators()) {
                    eg.disable();
                }
            }
        } else if (this.restarting == 0) {
            this.getArena().restart();
            this.task.cancel();
            this.arena = null;
        }
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }
}

