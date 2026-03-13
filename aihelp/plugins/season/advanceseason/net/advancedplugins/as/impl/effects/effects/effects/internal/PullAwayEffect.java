/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.util.NumberConversions
 *  org.bukkit.util.Vector
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class PullAwayEffect
extends AdvancedEffect {
    public PullAwayEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "PULL_AWAY", "Pull away an entity", "%e:<DISTANCE>");
        this.addArgument(0, Double.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        LivingEntity livingEntity2 = this.getOtherEntity(livingEntity, executionTask);
        try {
            double d = Double.parseDouble(stringArray[0]);
            Vector vector = livingEntity.getLocation().toVector().subtract(livingEntity2.getLocation().toVector()).normalize().multiply(d);
            if (!NumberConversions.isFinite((double)vector.getX())) {
                return false;
            }
            if (ASManager.isExcessVelocity(vector)) {
                return false;
            }
            livingEntity.setVelocity(vector);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }
}

