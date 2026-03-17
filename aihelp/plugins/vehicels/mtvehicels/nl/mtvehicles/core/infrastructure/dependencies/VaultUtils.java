/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.RegisteredServiceProvider
 */
package nl.mtvehicles.core.infrastructure.dependencies;

import net.milkbowl.vault.economy.Economy;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultUtils {
    private static Economy economy = null;

    public VaultUtils() {
        if (!this.setupEconomy()) {
            Bukkit.getLogger().warning("MTVehicles connected to Vault successfully, but found no linked economy plugin.");
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = (Economy)rsp.getProvider();
        return economy != null;
    }

    public boolean retryEconomySetup() {
        economy = null;
        return this.setupEconomy();
    }

    public boolean isEconomySetUp() {
        return economy != null;
    }

    public String getEconomyName() {
        return economy.getName();
    }

    public boolean depositMoneyPlayer(OfflinePlayer p, double amount) {
        if (!this.isPriceOk(amount) || !this.isEconomySetUp()) {
            return false;
        }
        if (!economy.hasAccount(p)) {
            return false;
        }
        return economy.depositPlayer(p, amount).transactionSuccess();
    }

    public boolean withdrawMoneyPlayer(OfflinePlayer p, double amount) {
        if (!this.isPriceOk(amount) || !this.isEconomySetUp()) {
            return false;
        }
        if (!economy.hasAccount(p)) {
            return false;
        }
        if (!economy.has(p, amount)) {
            if (p.isOnline()) {
                ConfigModule.messagesConfig.sendMessage((CommandSender)p.getPlayer(), Message.INSUFFICIENT_FUNDS);
            }
            return false;
        }
        if (economy.withdrawPlayer(p, amount).transactionSuccess()) {
            if (p.isOnline()) {
                p.getPlayer().sendMessage(String.format(ConfigModule.messagesConfig.getMessage(Message.TRANSACTION_SUCCESSFUL), DependencyModule.vault.getMoneyFormat(amount)));
            }
            return true;
        }
        return false;
    }

    public String getMoneyFormat(double amount) {
        return economy.format(amount);
    }

    private boolean isPriceOk(double price) {
        return price > 0.0;
    }
}

