/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class StealHealthEffect
extends AdvancedEffect {
    public StealHealthEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "STEAL_HEALTH", "Steal health from one entity for another", "%e:<HEALTH>");
        this.addArgument(0, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        LivingEntity livingEntity2 = this.getOtherEntity(livingEntity, executionTask);
        double d = Double.parseDouble(stringArray[0]);
        if (executionTask.getBuilder().isDamageEventNotGoingToRun()) {
            livingEntity.setMetadata("ae_damage_event_not_going_to_run", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
        }
        executionTask.getDamageHandler().damage((Damageable)livingEntity2, (Entity)livingEntity, d);
        executionTask.getDamageHandler().heal((Entity)livingEntity, d);
        if (executionTask.getBuilder().isDamageEventNotGoingToRun()) {
            livingEntity.removeMetadata("ae_damage_event_not_going_to_run", (Plugin)EffectsHandler.getInstance());
        }
        return true;
    }
}

