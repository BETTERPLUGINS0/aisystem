/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.concurrent.ThreadLocalRandom;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class DisarmEffect
extends AdvancedEffect {
    public DisarmEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "DISARM", "Disarm Entity", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        ItemStack itemStack = livingEntity.getEquipment().getItemInMainHand();
        if (!ASManager.isValid(itemStack)) {
            return false;
        }
        livingEntity.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
        if (livingEntity instanceof Player) {
            ItemStack itemStack2;
            int n;
            Player player = (Player)livingEntity;
            int n2 = player.getInventory().getHeldItemSlot();
            int n3 = 0;
            int n4 = 10;
            do {
                n = ThreadLocalRandom.current().nextInt(36);
                itemStack2 = player.getInventory().getItem(n);
            } while (n == n2 && ++n3 < 10);
            if (n3 >= 10) {
                return true;
            }
            player.getInventory().setItem(n, itemStack);
            player.getInventory().setItem(n2, itemStack2);
        } else {
            ASManager.dropItem(livingEntity.getLocation(), itemStack);
        }
        return true;
    }
}

