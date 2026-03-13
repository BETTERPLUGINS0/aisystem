/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.economy.local;

import net.advancedplugins.as.impl.utils.ExperienceManager;
import net.advancedplugins.as.impl.utils.economy.AdvancedEconomy;
import org.bukkit.entity.Player;

public class ExpEconomy
implements AdvancedEconomy {
    @Override
    public String getName() {
        return "EXP";
    }

    @Override
    public boolean chargeUser(Player player, double d) {
        ExperienceManager experienceManager = new ExperienceManager(player);
        if ((double)experienceManager.getTotalExperience() < d) {
            return false;
        }
        experienceManager.setTotalExperience((int)((double)experienceManager.getTotalExperience() - d));
        return true;
    }

    @Override
    public double getBalance(Player player) {
        return new ExperienceManager(player).getTotalExperience();
    }

    @Override
    public boolean giveUser(Player player, double d) {
        ExperienceManager experienceManager = new ExperienceManager(player);
        experienceManager.setTotalExperience((int)((double)experienceManager.getTotalExperience() - d));
        return true;
    }
}

