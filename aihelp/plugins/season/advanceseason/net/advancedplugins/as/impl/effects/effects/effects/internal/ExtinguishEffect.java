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
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class ExtinguishEffect
extends AdvancedEffect {
    public ExtinguishEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "EXTINGUISH", "Extinguish an entity", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        SchedulerUtils.runTaskLater(() -> livingEntity.setFireTicks(0), 1L);
        return true;
    }
}

