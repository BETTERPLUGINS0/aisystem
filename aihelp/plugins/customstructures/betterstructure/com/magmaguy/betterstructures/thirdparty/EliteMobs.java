/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.magmaguy.elitemobs.commands.ReloadCommand
 *  com.magmaguy.elitemobs.mobconstructor.custombosses.RegionalBossEntity
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.magmaguy.betterstructures.thirdparty;

import com.magmaguy.elitemobs.commands.ReloadCommand;
import com.magmaguy.elitemobs.mobconstructor.custombosses.RegionalBossEntity;
import com.magmaguy.magmacore.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EliteMobs {
    public static boolean Spawn(Location location, String filename) {
        if (Bukkit.getPluginManager().getPlugin("EliteMobs") != null) {
            RegionalBossEntity regionalBossEntity = RegionalBossEntity.SpawnRegionalBoss((String)filename, (Location)location);
            if (regionalBossEntity == null) {
                Logger.warn("Failed to spawn regional boss " + filename + "! The filename for this boss probably does not match the filename that should be in ~/plugins/EliteMobs/custombosses/");
                return false;
            }
            regionalBossEntity.spawn(false);
            return true;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("betterstructures.*")) continue;
            Logger.sendMessage((CommandSender)player, "&cOne of your packs uses the EliteMobs plugin &4but EliteMobs is not currently installed on your server&c! &2You can download it here: &9https://nightbreak.io/plugin/elitemobs/");
        }
        return false;
    }

    public static void Reload() {
        ReloadCommand.reload((CommandSender)Bukkit.getConsoleSender());
    }
}

