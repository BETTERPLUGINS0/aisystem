/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.premium;

import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PremiumHandler {
    @Contract(pure=true)
    @NotNull
    public static String getUser() {
        return "1026558";
    }

    public static boolean isPremium() {
        return !PremiumHandler.getUser().contains("__USER__");
    }

    public static void sendMessage(@NotNull CommandSender commandSender) {
        if (!PremiumHandler.getUser().contains("__USER__")) {
            commandSender.sendMessage(String.format("%s Running %s v%s. Licensed to %s", Main.prefix, QualityArmoryVehicles.getPlugin().getName(), QualityArmoryVehicles.getPlugin().getDescription().getVersion(), ChatColor.YELLOW + " https://www.spigotmc.org/members/" + PremiumHandler.getUser()));
            return;
        }
        commandSender.sendMessage(String.format("%s " + ChatColor.RED + "Error: This plugin is not signed with a license.", Main.prefix));
    }
}

