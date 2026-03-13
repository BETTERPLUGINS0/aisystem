/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class DropHeldItemEffect
extends AdvancedEffect {
    public DropHeldItemEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "DROP_HELD_ITEM", "Make entity drop held item to ground", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (livingEntity.getEquipment() == null || livingEntity.getEquipment().getItemInMainHand() == null) {
            return true;
        }
        ItemStack itemStack = livingEntity.getEquipment().getItemInMainHand().clone();
        livingEntity.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
        ASManager.dropItem(livingEntity.getLocation(), itemStack);
        return true;
    }
}

