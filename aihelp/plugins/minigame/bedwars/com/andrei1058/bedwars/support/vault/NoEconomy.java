/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.support.vault;

import com.andrei1058.bedwars.support.vault.Economy;
import org.bukkit.entity.Player;

public class NoEconomy
implements Economy {
    @Override
    public boolean isEconomy() {
        return false;
    }

    @Override
    public double getMoney(Player p) {
        return 0.0;
    }

    @Override
    public void giveMoney(Player p, double money) {
        p.sendMessage("\u00a7cVault support missing!");
    }

    @Override
    public void buyAction(Player p, double cost) {
        p.sendMessage("\u00a7cVault support missing!");
    }
}

