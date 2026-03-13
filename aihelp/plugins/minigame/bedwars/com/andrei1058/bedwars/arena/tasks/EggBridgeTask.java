/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Egg
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package com.andrei1058.bedwars.arena.tasks;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.events.gameplay.EggBridgeBuildEvent;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.listeners.EggBridge;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class EggBridgeTask
implements Runnable {
    private Egg projectile;
    private TeamColor teamColor;
    private Player player;
    private IArena arena;
    private BukkitTask task;

    public EggBridgeTask(Player player, Egg projectile, TeamColor teamColor) {
        IArena a = Arena.getArenaByPlayer(player);
        if (a == null) {
            return;
        }
        this.arena = a;
        this.projectile = projectile;
        this.teamColor = teamColor;
        this.player = player;
        this.task = Bukkit.getScheduler().runTaskTimer((Plugin)BedWars.plugin, (Runnable)this, 0L, 1L);
    }

    public TeamColor getTeamColor() {
        return this.teamColor;
    }

    public Egg getProjectile() {
        return this.projectile;
    }

    public Player getPlayer() {
        return this.player;
    }

    public IArena getArena() {
        return this.arena;
    }

    @Override
    public void run() {
        Location loc = this.getProjectile().getLocation();
        if (this.getProjectile().isDead() || !this.arena.isPlayer(this.getPlayer()) || this.getPlayer().getLocation().distance(this.getProjectile().getLocation()) > 27.0 || this.getPlayer().getLocation().getY() - this.getProjectile().getLocation().getY() > 9.0) {
            EggBridge.removeEgg(this.projectile);
            return;
        }
        if (this.getPlayer().getLocation().distance(loc) > 4.0) {
            Block b4;
            Block b3;
            Block b2 = loc.clone().subtract(0.0, 2.0, 0.0).getBlock();
            if (!Misc.isBuildProtected(b2.getLocation(), this.getArena()) && b2.getType() == Material.AIR) {
                b2.setType(BedWars.nms.woolMaterial());
                BedWars.nms.setBlockTeamColor(b2, this.getTeamColor());
                this.getArena().addPlacedBlock(b2);
                Bukkit.getPluginManager().callEvent((Event)new EggBridgeBuildEvent(this.getTeamColor(), this.getArena(), b2));
                loc.getWorld().playEffect(b2.getLocation(), BedWars.nms.eggBridge(), 3);
                Sounds.playSound("egg-bridge-block", this.getPlayer());
            }
            if (!Misc.isBuildProtected((b3 = loc.clone().subtract(1.0, 2.0, 0.0).getBlock()).getLocation(), this.getArena()) && b3.getType() == Material.AIR) {
                b3.setType(BedWars.nms.woolMaterial());
                BedWars.nms.setBlockTeamColor(b3, this.getTeamColor());
                this.getArena().addPlacedBlock(b3);
                Bukkit.getPluginManager().callEvent((Event)new EggBridgeBuildEvent(this.getTeamColor(), this.getArena(), b3));
                loc.getWorld().playEffect(b3.getLocation(), BedWars.nms.eggBridge(), 3);
                Sounds.playSound("egg-bridge-block", this.getPlayer());
            }
            if (!Misc.isBuildProtected((b4 = loc.clone().subtract(0.0, 2.0, 1.0).getBlock()).getLocation(), this.getArena()) && b4.getType() == Material.AIR) {
                b4.setType(BedWars.nms.woolMaterial());
                BedWars.nms.setBlockTeamColor(b4, this.getTeamColor());
                this.getArena().addPlacedBlock(b4);
                Bukkit.getPluginManager().callEvent((Event)new EggBridgeBuildEvent(this.getTeamColor(), this.getArena(), b4));
                loc.getWorld().playEffect(b4.getLocation(), BedWars.nms.eggBridge(), 3);
                Sounds.playSound("egg-bridge-block", this.getPlayer());
            }
        }
    }

    public void cancel() {
        this.task.cancel();
    }
}

