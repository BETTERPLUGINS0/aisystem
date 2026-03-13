/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.andrei1058.bedwars.arena.upgrades;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HealPoolTask
extends BukkitRunnable {
    private ITeam bwt;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private int maxZ;
    private int minZ;
    private IArena arena;
    private Random r = new Random();
    private Location l;
    private static List<HealPoolTask> healPoolTasks = new ArrayList<HealPoolTask>();

    public HealPoolTask(ITeam bwt) {
        this.bwt = bwt;
        if (bwt == null || bwt.getSpawn() == null) {
            HealPoolTask.removeForTeam(this.bwt);
            this.cancel();
            return;
        }
        int radius = bwt.getArena().getConfig().getInt("island-radius");
        Location teamspawn = bwt.getSpawn();
        this.maxX = teamspawn.getBlockX() + radius;
        this.minX = teamspawn.getBlockX() - radius;
        this.maxY = teamspawn.getBlockY() + radius;
        this.minY = teamspawn.getBlockY() - radius;
        this.maxZ = teamspawn.getBlockZ() + radius;
        this.minZ = teamspawn.getBlockZ() - radius;
        this.arena = bwt.getArena();
        this.runTaskTimerAsynchronously((Plugin)BedWars.plugin, 0L, 80L);
        healPoolTasks.add(this);
    }

    public void run() {
        if (this.bwt == null || this.bwt.getSpawn() == null || this.arena == null) {
            healPoolTasks.remove((Object)this);
            return;
        }
        for (int x = this.minX; x <= this.maxX; ++x) {
            for (int y = this.minY; y <= this.maxY; ++y) {
                for (int z = this.minZ; z <= this.maxZ; ++z) {
                    int chance;
                    this.l = new Location(this.arena.getWorld(), (double)x + 0.5, (double)y + 0.5, (double)z + 0.5);
                    if (this.l.getBlock().getType() != Material.AIR || (chance = this.r.nextInt(9)) != 0) continue;
                    if (BedWars.config.getBoolean("performance-settings.heal-pool.seen-by-team-only")) {
                        for (Player p : this.bwt.getMembers()) {
                            BedWars.nms.playVillagerEffect(p, this.l);
                        }
                        continue;
                    }
                    for (Player p : this.arena.getPlayers()) {
                        BedWars.nms.playVillagerEffect(p, this.l);
                    }
                }
            }
        }
    }

    public static boolean exists(IArena arena, ITeam bwt) {
        if (healPoolTasks.isEmpty()) {
            return false;
        }
        for (HealPoolTask hpt : healPoolTasks) {
            if (hpt.getArena() != arena || hpt.getBwt() != bwt) continue;
            return true;
        }
        return false;
    }

    public static void removeForArena(IArena a) {
        if (healPoolTasks.isEmpty() || a == null) {
            return;
        }
        for (HealPoolTask hpt : healPoolTasks) {
            if (hpt == null || !hpt.getArena().equals(a)) continue;
            hpt.cancel();
            healPoolTasks.remove((Object)hpt);
        }
    }

    public static void removeForArena(String a) {
        if (healPoolTasks == null || healPoolTasks.isEmpty() || a == null) {
            return;
        }
        for (HealPoolTask hpt : healPoolTasks) {
            if (hpt == null || !hpt.getArena().getWorldName().equals(a)) continue;
            hpt.cancel();
            healPoolTasks.remove((Object)hpt);
        }
    }

    public static void removeForTeam(ITeam team) {
        if (healPoolTasks == null || healPoolTasks.isEmpty() || team == null) {
            return;
        }
        for (HealPoolTask hpt : healPoolTasks) {
            if (hpt == null || !hpt.getBwt().equals(team)) continue;
            hpt.cancel();
            healPoolTasks.remove((Object)hpt);
        }
    }

    public ITeam getBwt() {
        return this.bwt;
    }

    public IArena getArena() {
        return this.arena;
    }
}

