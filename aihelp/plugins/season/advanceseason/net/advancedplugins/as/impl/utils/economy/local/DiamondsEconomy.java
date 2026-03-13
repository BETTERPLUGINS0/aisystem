/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.ae.utils.AManager
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.utils.economy.local;

import java.util.Collections;
import net.advancedplugins.ae.utils.AManager;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.economy.AdvancedEconomy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DiamondsEconomy
implements AdvancedEconomy {
    @Override
    public String getName() {
        return "DIAMONDS";
    }

    @Override
    public boolean chargeUser(Player player, double d) {
        if (!AManager.hasAmount((Player)player, (Material)Material.DIAMOND, (int)((int)d))) {
            return false;
        }
        AManager.removeItems((Inventory)player.getInventory(), (Material)Material.DIAMOND, (int)((int)d));
        return true;
    }

    @Override
    public double getBalance(Player player) {
        return AManager.getAmount((Player)player, (Material)Material.DIAMOND);
    }

    @Override
    public boolean giveUser(Player player, double d) {
        ASManager.giveItem(player, (ItemStack[])Collections.nCopies((int)d, Material.DIAMOND).stream().map(ItemStack::new).toArray(ItemStack[]::new));
        return true;
    }
}

