/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class IgnoreArmorDamageEffect
extends AdvancedEffect {
    public IgnoreArmorDamageEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "IGNORE_ARMOR_DAMAGE", "Cancel armor durability reduction", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        short[] sArray = new short[]{entityEquipment.getHelmet() == null ? (short)0 : entityEquipment.getHelmet().getDurability(), entityEquipment.getChestplate() == null ? (short)0 : entityEquipment.getChestplate().getDurability(), entityEquipment.getLeggings() == null ? (short)0 : entityEquipment.getLeggings().getDurability(), entityEquipment.getBoots() == null ? (short)0 : entityEquipment.getBoots().getDurability()};
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)EffectsHandler.getInstance(), () -> {
            if (entityEquipment.getHelmet() != null) {
                entityEquipment.getHelmet().setDurability((short)(sArray[0] + 1));
            }
            if (entityEquipment.getChestplate() != null) {
                entityEquipment.getChestplate().setDurability((short)(sArray[1] + 1));
            }
            if (entityEquipment.getLeggings() != null) {
                entityEquipment.getLeggings().setDurability((short)(sArray[2] + 1));
            }
            if (entityEquipment.getBoots() != null) {
                entityEquipment.getBoots().setDurability((short)(sArray[3] + 1));
            }
        }, 0L);
        return true;
    }
}

