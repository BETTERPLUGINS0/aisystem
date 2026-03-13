/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class SmeltEffect
extends AdvancedEffect {
    public SmeltEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "SMELT", "Smelt drops", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        executionTask.getBuilder().getDrops().getSettings().setSmelt(true);
        executionTask.getBuilder().getDrops().getSettings().setBreakBlocks(true);
        return true;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        executionTask.getBuilder().getDrops().getSettings().setSmelt(true);
        executionTask.getBuilder().getDrops().getSettings().setBreakBlocks(true);
        return true;
    }
}

