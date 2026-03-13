/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.economy.local;

import net.advancedplugins.as.impl.utils.economy.AdvancedEconomy;
import org.bukkit.entity.Player;

public class LevelEconomy
implements AdvancedEconomy {
    @Override
    public String getName() {
        return "LEVEL";
    }

    @Override
    public boolean chargeUser(Player player, double d) {
        if ((double)player.getLevel() < d) {
            return false;
        }
        player.setLevel((int)((double)player.getLevel() - d));
        return true;
    }

    @Override
    public double getBalance(Player player) {
        return player.getLevel();
    }

    @Override
    public boolean giveUser(Player player, double d) {
        player.setLevel((int)((double)player.getLevel() + d));
        return true;
    }
}

