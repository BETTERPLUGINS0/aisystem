/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.ItemDurability;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class AddDurabilityCurrentItemEffect
extends AdvancedEffect {
    public AddDurabilityCurrentItemEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "ADD_DURABILITY_CURRENT_ITEM", "Add durability to the item used", "%e:<AMOUNT>");
        this.addArgument(0, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        int n = ASManager.parseInt(stringArray[0]);
        ItemDurability itemDurability = new ItemDurability(livingEntity, executionTask.getBuilder().getItem());
        itemDurability.handleDurabilityChange(n);
        AddDurabilityCurrentItemEffect.updateItem(executionTask.getBuilder(), livingEntity, itemDurability.isBroken() ? null : itemDurability.getItemStack(), executionTask.getBuilder().getItemType());
        return true;
    }
}

