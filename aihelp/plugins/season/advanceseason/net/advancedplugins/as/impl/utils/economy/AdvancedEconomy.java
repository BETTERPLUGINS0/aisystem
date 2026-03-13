/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.economy;

import org.bukkit.entity.Player;

public interface AdvancedEconomy {
    public String getName();

    public boolean chargeUser(Player var1, double var2);

    public double getBalance(Player var1);

    public boolean giveUser(Player var1, double var2);
}

