/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.lumine.mythic.api.mobs.MythicMob
 *  io.lumine.mythic.bukkit.BukkitAdapter
 *  io.lumine.mythic.bukkit.MythicBukkit
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.magmaguy.betterstructures.thirdparty;

import com.magmaguy.magmacore.util.Logger;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MythicMobs {
    public static boolean Spawn(Location location, String filename) {
        double level;
        if (Bukkit.getPluginManager().getPlugin("MythicMobs") == null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("betterstructures.*")) continue;
                Logger.sendMessage((CommandSender)player, "&cOne of your packs uses the MythicMobs plugin &4but MythicMobs is not currently installed on your server&c! &2You can download it here: &9https://www.spigotmc.org/resources/%E2%9A%94-mythicmobs-free-version-%E2%96%BAthe-1-custom-mob-creator%E2%97%84.5702/");
            }
            return false;
        }
        String[] args2 = filename.split(":");
        MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(args2[0]).orElse(null);
        if (mob == null) {
            Logger.warn("Failed to spawn regional boss " + args2[0] + "! The filename for this boss probably does not match the mob that should be in ~/plugins/MythicMobs/Mobs/");
            return false;
        }
        try {
            level = Double.parseDouble(args2[1]);
        } catch (Exception e) {
            Logger.warn("Failed to parse level for mob " + filename + "!");
            return false;
        }
        mob.spawn(BukkitAdapter.adapt((Location)location), Math.max(1.0, level));
        return true;
    }
}

