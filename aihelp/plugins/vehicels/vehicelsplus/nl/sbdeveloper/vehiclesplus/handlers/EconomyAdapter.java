/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  net.milkbowl.vault.economy.EconomyResponse
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.RegisteredServiceProvider
 */
package nl.sbdeveloper.vehiclesplus.handlers;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyAdapter {
    private static Economy economy;

    private EconomyAdapter() {
    }

    public static boolean load() {
        if (!Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {
            return false;
        }
        RegisteredServiceProvider registeredServiceProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (registeredServiceProvider == null) {
            return false;
        }
        economy = (Economy)registeredServiceProvider.getProvider();
        return true;
    }

    public static boolean isLoaded() {
        return economy != null;
    }

    public static boolean withdraw(Player player, double d) {
        if (!EconomyAdapter.isLoaded()) {
            player.sendMessage(Locale.getMessage(PluginMessage.GENERAL_ERRORS_NOECONOMY));
            return false;
        }
        EconomyResponse economyResponse = economy.withdrawPlayer((OfflinePlayer)player, d);
        if (!economyResponse.transactionSuccess()) {
            player.sendMessage(Locale.getMessage(PluginMessage.GENERAL_ERRORS_NOTENOUGHMONEY));
            return false;
        }
        return true;
    }
}

