/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.TotemUndying;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public class TotemEffect
extends AdvancedEffect {
    public TotemEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "TOTEM", "Activate the Totem of Undying effect", "%e:[TICKS]:[TICKS]:[TICKS]");
        this.addArgument(0, Integer.class);
        this.addArgument(1, Integer.class);
        this.addArgument(2, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        int n = stringArray.length > 0 ? ASManager.parseInt(stringArray[0], 900) : 900;
        int n2 = stringArray.length > 1 ? ASManager.parseInt(stringArray[1], 800) : 800;
        int n3 = stringArray.length > 2 ? ASManager.parseInt(stringArray[2], 100) : 100;
        new TotemUndying().playEffect(livingEntity, EquipmentSlot.HAND, n, n2, n3, false);
        return true;
    }
}

