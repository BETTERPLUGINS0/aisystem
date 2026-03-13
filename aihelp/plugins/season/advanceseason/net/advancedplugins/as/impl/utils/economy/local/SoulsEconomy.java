/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.ae.features.souls.SoulsAPI
 *  net.advancedplugins.ae.utils.ItemInHand
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.utils.economy.local;

import net.advancedplugins.ae.features.souls.SoulsAPI;
import net.advancedplugins.ae.utils.ItemInHand;
import net.advancedplugins.as.impl.utils.economy.AdvancedEconomy;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SoulsEconomy
implements AdvancedEconomy {
    @Override
    public String getName() {
        return "SOULS";
    }

    @Override
    public boolean chargeUser(Player player, double d) {
        ItemStack itemStack = new ItemInHand((LivingEntity)player).get();
        if (this.getBalance(player) < d) {
            return false;
        }
        new ItemInHand((LivingEntity)player).set(SoulsAPI.useSouls((ItemStack)itemStack, (int)((int)d)));
        return true;
    }

    @Override
    public double getBalance(Player player) {
        ItemStack itemStack = new ItemInHand((LivingEntity)player).get();
        return SoulsAPI.getSoulsOnItem((ItemStack)itemStack);
    }

    @Override
    public boolean giveUser(Player player, double d) {
        ItemStack itemStack = new ItemInHand((LivingEntity)player).get();
        new ItemInHand((LivingEntity)player).set(SoulsAPI.addSouls((ItemStack)itemStack, (int)((int)d)));
        return true;
    }
}

