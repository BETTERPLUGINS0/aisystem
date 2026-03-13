/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.support.vault;

import com.andrei1058.bedwars.support.vault.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class WithEconomy
implements Economy {
    private static net.milkbowl.vault.economy.Economy economy;

    @Override
    public boolean isEconomy() {
        return true;
    }

    @Override
    public double getMoney(Player p) {
        return economy.getBalance((OfflinePlayer)p);
    }

    @Override
    public void giveMoney(Player p, double money) {
        economy.depositPlayer((OfflinePlayer)p, money);
    }

    @Override
    public void buyAction(Player p, double cost) {
        economy.bankWithdraw(p.getName(), cost);
    }

    public static void setEconomy(net.milkbowl.vault.economy.Economy economy) {
        WithEconomy.economy = economy;
    }
}

