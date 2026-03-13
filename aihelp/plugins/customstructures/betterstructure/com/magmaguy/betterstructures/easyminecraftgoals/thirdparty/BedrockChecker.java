/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package com.magmaguy.betterstructures.easyminecraftgoals.thirdparty;

import com.magmaguy.betterstructures.easyminecraftgoals.thirdparty.Floodgate;
import com.magmaguy.betterstructures.easyminecraftgoals.thirdparty.Geyser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BedrockChecker {
    private BedrockChecker() {
    }

    public static boolean isBedrock(Player player) {
        if (Bukkit.getPluginManager().isPluginEnabled("floodgate")) {
            return Floodgate.isBedrock(player);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Geyser-Spigot")) {
            return Geyser.isBedrock(player);
        }
        return false;
    }
}

