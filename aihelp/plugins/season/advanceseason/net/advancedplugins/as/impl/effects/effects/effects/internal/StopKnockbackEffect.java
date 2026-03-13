/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.util.Vector
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class StopKnockbackEffect
extends AdvancedEffect {
    public StopKnockbackEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "STOP_KNOCKBACK", "Stop entity's knockback", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        SchedulerUtils.runTaskLater(() -> livingEntity.setVelocity(new Vector(0, 0, 0)));
        return true;
    }
}

