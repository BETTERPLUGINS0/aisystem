/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.Objects;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ItemDurability;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class RepairEffect
extends AdvancedEffect {
    public RepairEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "REPAIR", "Repair item", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (livingEntity.isDead() || !livingEntity.isValid()) {
            return true;
        }
        if (Objects.equals(executionTask.getBuilder().getItem().getType(), Material.TRIDENT)) {
            return true;
        }
        ItemDurability itemDurability = new ItemDurability(livingEntity, executionTask.getBuilder().getItem());
        itemDurability.repairItem();
        RepairEffect.updateItem(executionTask.getBuilder(), livingEntity, itemDurability.getItemStack(), executionTask.getBuilder().getItemType());
        return true;
    }
}

