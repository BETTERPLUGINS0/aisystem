/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.support.vault;

import org.bukkit.entity.Player;

public interface Economy {
    public boolean isEconomy();

    public double getMoney(Player var1);

    public void giveMoney(Player var1, double var2);

    public void buyAction(Player var1, double var2);
}

