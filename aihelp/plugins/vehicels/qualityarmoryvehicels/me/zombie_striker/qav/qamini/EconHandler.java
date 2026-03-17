/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.RegisteredServiceProvider
 */
package me.zombie_striker.qav.qamini;

import me.zombie_striker.qav.api.QualityArmoryVehicles;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconHandler {
    public static Economy econ;

    public static void setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider registeredServiceProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (registeredServiceProvider == null) {
            return;
        }
        econ = (Economy)registeredServiceProvider.getProvider();
        QualityArmoryVehicles.getPlugin().getLogger().info("Hooked with Vault");
    }

    public static boolean hasEnough(int n, Player player) {
        return !EconHandler.isVault() || econ.getBalance((OfflinePlayer)player) >= (double)n;
    }

    public static void pay(int n, Player player) {
        if (EconHandler.isVault()) {
            econ.withdrawPlayer((OfflinePlayer)player, (double)n);
        }
    }

    public static void deposit(int n, Player player) {
        if (EconHandler.isVault()) {
            econ.depositPlayer((OfflinePlayer)player, (double)n);
        }
    }

    public static boolean isVault() {
        return econ != null;
    }
}

