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

public class PullCloserEffect
extends AdvancedEffect {
    public PullCloserEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "PULL_CLOSER", "Pull an entity closer", "%e:<AMOUNT>");
        this.addArgument(0, Double.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        LivingEntity livingEntity2 = this.getOtherEntity(livingEntity, executionTask);
        if (livingEntity == null || livingEntity2 == null) {
            return true;
        }
        try {
            double d = Double.parseDouble(stringArray[0]);
            Vector vector = livingEntity2.getLocation().toVector().subtract(livingEntity.getLocation().toVector()).normalize().multiply(d);
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

