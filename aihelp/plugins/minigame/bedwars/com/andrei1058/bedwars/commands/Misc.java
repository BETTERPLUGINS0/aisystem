/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.commands;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Misc {
    public static void createArmorStand(String name, @NotNull Location location, String configLoc) {
        ArmorStand a = (ArmorStand)location.getWorld().spawnEntity(location.getBlock().getLocation().add(0.5, 2.0, 0.5), EntityType.ARMOR_STAND);
        a.setVisible(false);
        a.setMarker(true);
        a.setGravity(false);
        a.setCustomNameVisible(true);
        a.setCustomName(name);
        a.setMetadata("bw1058-setup", (MetadataValue)new FixedMetadataValue((Plugin)BedWars.plugin, (Object)"hologram"));
        if (configLoc != null) {
            a.setMetadata("bw1058-loc", (MetadataValue)new FixedMetadataValue((Plugin)BedWars.plugin, (Object)configLoc));
        }
    }

    public static void removeArmorStand(String contains, @NotNull Location location, String configLoc) {
        for (Entity e : location.getWorld().getNearbyEntities(location, 1.0, 3.0, 1.0)) {
            if (e.hasMetadata("bw1058-setup")) {
                if (e.hasMetadata("bw1058-loc")) {
                    if (!((MetadataValue)e.getMetadata("bw1058-loc").get(0)).asString().equalsIgnoreCase(configLoc)) continue;
                    if (contains != null && !contains.isEmpty() && ChatColor.stripColor((String)e.getCustomName()).contains(contains)) {
                        e.remove();
                        return;
                    }
                    e.remove();
                    continue;
                }
                e.remove();
                continue;
            }
            if (e.getType() != EntityType.ARMOR_STAND || ((ArmorStand)e).isVisible() || contains == null || !e.getCustomName().contains(contains)) continue;
            e.remove();
        }
    }

    public static void autoSetGen(Player p, String command, SetupSession setupSession, Material type) {
        if (type == Material.EMERALD_BLOCK) {
            if (setupSession.isAutoCreatedEmerald()) {
                return;
            }
            setupSession.setAutoCreatedEmerald(true);
        } else {
            if (setupSession.isAutoCreatedDiamond()) {
                return;
            }
            setupSession.setAutoCreatedDiamond(true);
        }
        Misc.detectGenerators(p.getLocation().add(0.0, -1.0, 0.0).getBlock().getLocation(), setupSession);
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
            for (Location l : setupSession.getSkipAutoCreateGen()) {
                Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
                    TeleportManager.teleport((Entity)p, l);
                    Bukkit.dispatchCommand((CommandSender)p, (String)(command + (l.add(0.0, -1.0, 0.0).getBlock().getType() == Material.EMERALD_BLOCK ? "emerald" : "diamond")));
                }, 20L);
            }
        }, 20L);
    }

    private static void detectGenerators(Location origin, SetupSession setupSession) {
        origin = origin.getBlock().getLocation();
        setupSession.addSkipAutoCreateGen(origin);
        Material target = origin.getBlock().getType();
        Material layout_z_minus = origin.clone().add(0.0, 1.0, -1.0).getBlock().getType();
        Material layout_z_plus = origin.clone().add(0.0, 1.0, 1.0).getBlock().getType();
        Material layout_x_minus = origin.clone().add(-1.0, 1.0, 0.0).getBlock().getType();
        Material layout_x_plus = origin.clone().add(1.0, 1.0, 0.0).getBlock().getType();
        Material layout_x_plus_z_plus = origin.clone().add(1.0, 1.0, 1.0).getBlock().getType();
        Material layout_x_plus_z_minus = origin.clone().add(1.0, 1.0, -1.0).getBlock().getType();
        Material layout_x_minus_z_plus = origin.clone().add(-1.0, 1.0, 1.0).getBlock().getType();
        Material layout_x_minus_z_minus = origin.clone().add(-1.0, 1.0, -1.0).getBlock().getType();
        String path = "generator." + (target == Material.DIAMOND_BLOCK ? "Diamond" : "Emerald");
        if (layout_z_minus == Material.AIR || layout_z_plus == Material.AIR || layout_x_minus == Material.AIR || layout_x_plus == Material.AIR || layout_x_plus_z_plus == Material.AIR || layout_x_plus_z_minus == Material.AIR || layout_x_minus_z_plus == Material.AIR || layout_x_minus_z_minus == Material.AIR) {
            return;
        }
        List<Location> locations = setupSession.getConfig().getArenaLocations(path);
        for (int x = -150; x < 150; ++x) {
            for (int z = -150; z < 150; ++z) {
                Block b = origin.clone().add((double)x, 0.0, (double)z).getBlock();
                if (b.getX() == origin.getBlockX() && b.getY() == origin.getBlockY() && b.getZ() == origin.getBlockZ()) continue;
                Location l = b.getLocation().clone().add(0.0, 1.0, 0.0);
                for (Location location : locations) {
                    setupSession.getConfig().compareArenaLoc(location, b.getLocation().add(0.0, 1.0, 0.0));
                }
                if (b.getType() != target || layout_z_minus != l.clone().add(0.0, 0.0, -1.0).getBlock().getType() || layout_z_plus != l.clone().add(0.0, 0.0, 1.0).getBlock().getType() || layout_x_minus != l.clone().add(-1.0, 0.0, 0.0).getBlock().getType() || layout_x_plus != l.clone().add(1.0, 0.0, 0.0).getBlock().getType() || layout_x_plus_z_minus != l.clone().add(1.0, 0.0, -1.0).getBlock().getType() || layout_x_plus_z_plus != l.clone().add(1.0, 0.0, 1.0).getBlock().getType() || layout_x_minus_z_plus != l.clone().add(-1.0, 0.0, 1.0).getBlock().getType() || layout_x_minus_z_minus != l.clone().add(-1.0, 0.0, -1.0).getBlock().getType() || setupSession.getSkipAutoCreateGen().contains(l)) continue;
                setupSession.addSkipAutoCreateGen(l);
                Misc.detectGenerators(b.getLocation(), setupSession);
            }
        }
    }
}

