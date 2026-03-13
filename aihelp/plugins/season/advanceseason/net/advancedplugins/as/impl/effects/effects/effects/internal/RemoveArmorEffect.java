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

public class RemoveArmorEffect
extends AdvancedEffect {
    public RemoveArmorEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "REMOVE_ARMOR", "Remove a specific armor piece", "%e:<ARMOR TYPE>");
        this.addArgument(0, ArmorType.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        ArmorType armorType = ArmorType.valueOf(stringArray[0].toUpperCase());
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        ItemStack itemStack = null;
        switch (armorType) {
            case HELMET: {
                itemStack = entityEquipment.getHelmet();
                entityEquipment.setHelmet(new ItemStack(Material.AIR));
                break;
            }
            case CHESTPLATE: {
                itemStack = entityEquipment.getChestplate();
                entityEquipment.setChestplate(new ItemStack(Material.AIR));
                break;
            }
            case LEGGINGS: {
                itemStack = entityEquipment.getLeggings();
                entityEquipment.setLeggings(new ItemStack(Material.AIR));
                break;
            }
            case BOOTS: {
                itemStack = entityEquipment.getBoots();
                entityEquipment.setBoots(new ItemStack(Material.AIR));
            }
        }
        ArmorWearTrigger.getArmorWearTrigger().updateWornArmor(livingEntity, itemStack, new ItemStack(Material.AIR), armorType);
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

