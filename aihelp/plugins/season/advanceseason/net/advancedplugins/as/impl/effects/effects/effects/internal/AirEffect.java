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
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class AirEffect
extends AdvancedEffect {
    public AirEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "AIR", "Modify player's remaining air", "%e:<AMOUNT>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        int n = stringArray.length > 0 ? ASManager.parseInt(stringArray[0]) : livingEntity.getMaximumAir();
        livingEntity.setRemainingAir(Math.min(livingEntity.getMaximumAir(), n + livingEntity.getRemainingAir()));
        return true;
    }
}

