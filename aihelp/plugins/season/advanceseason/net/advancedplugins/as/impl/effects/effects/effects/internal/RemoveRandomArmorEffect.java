/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.concurrent.ThreadLocalRandom;
import net.advancedplugins.as.impl.effects.armorutils.ArmorType;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.ArmorWearTrigger;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoveRandomArmorEffect
extends AdvancedEffect {
    public RemoveRandomArmorEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "REMOVE_RANDOM_ARMOR", "Remove a random armor piece", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        ItemStack itemStack;
        double d = ThreadLocalRandom.current().nextDouble();
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        if (d <= 0.25) {
            itemStack = entityEquipment.getHelmet();
            entityEquipment.setHelmet(new ItemStack(Material.AIR));
        } else if (d <= 0.5) {
            itemStack = entityEquipment.getChestplate();
            entityEquipment.setChestplate(new ItemStack(Material.AIR));
        } else if (d <= 0.75) {
            itemStack = entityEquipment.getLeggings();
            entityEquipment.setLeggings(new ItemStack(Material.AIR));
        } else {
            itemStack = entityEquipment.getBoots();
            entityEquipment.setBoots(new ItemStack(Material.AIR));
        }
        if (itemStack != null) {
            ArmorWearTrigger.getArmorWearTrigger().updateWornArmor(livingEntity, itemStack, new ItemStack(Material.AIR), ArmorType.matchType(itemStack));
        }
        if (ASManager.isValid(itemStack)) {
            if (livingEntity instanceof Player) {
                ASManager.giveItem((Player)livingEntity, itemStack);
            } else {
                ASManager.dropItem(livingEntity.getLocation(), itemStack);
            }
        }
        return true;
    }
}

