/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.ItemDurability;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.plugin.java.JavaPlugin;

public class AddDurabilityItemEffect
extends AdvancedEffect {
    public AddDurabilityItemEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "ADD_DURABILITY_ITEM", "Add durability to the item", "%e:<AMOUNT/SLOT>:<AMOUNT>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (stringArray.length == 1) {
            int n = ASManager.parseInt(stringArray[0]);
            ItemDurability itemDurability = new ItemDurability(livingEntity, executionTask.getBuilder().getItem());
            itemDurability.handleDurabilityChange(n);
            if (executionTask.getBuilder().getEvent() instanceof BlockBreakEvent) {
                AddDurabilityItemEffect.updateItem(executionTask.getBuilder(), livingEntity, itemDurability.isBroken() ? null : itemDurability.getItemStack(), executionTask.getBuilder().getItemType());
            }
            return true;
        }
        try {
            RollItemType rollItemType = RollItemType.valueOf(stringArray[0]);
            int n = ASManager.parseInt(stringArray[1]);
            EntityEquipment entityEquipment = livingEntity.getEquipment();
            if (entityEquipment == null) {
                return true;
            }
            ItemDurability itemDurability = new ItemDurability(livingEntity, entityEquipment.getItem(rollItemType.getSlot()));
            itemDurability.handleDurabilityChange(n);
            if (executionTask.getBuilder().getEvent() instanceof BlockBreakEvent) {
                executionTask.getBuilder().setItem(itemDurability.getItemStack());
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
            return false;
        }
        return true;
    }
}

