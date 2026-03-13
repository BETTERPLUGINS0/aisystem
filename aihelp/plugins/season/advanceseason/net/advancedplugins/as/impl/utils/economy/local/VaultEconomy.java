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
package net.advancedplugins.as.impl.utils.economy.local;

import net.advancedplugins.as.impl.utils.economy.AdvancedEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultEconomy
implements AdvancedEconomy {
    private Economy econ;

    public VaultEconomy() {
        this.setupEconomy();
    }

    private boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider registeredServiceProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (registeredServiceProvider == null) {
            return false;
        }
        this.econ = (Economy)registeredServiceProvider.getProvider();
        return this.econ != null;
    }

    @Override
    public boolean giveUser(Player player, double d) {
        return this.econ.depositPlayer((OfflinePlayer)player, d).transactionSuccess();
    }

    @Override
    public String getName() {
        return "MONEY";
    }

    @Override
    public boolean chargeUser(Player player, double d) {
        if (this.getBalance(player) < d) {
            return false;
        }
        return this.econ.withdrawPlayer((OfflinePlayer)player, d).transactionSuccess();
    }

    @Override
    public double getBalance(Player player) {
        return this.econ.getBalance((OfflinePlayer)player);
    }
}

