/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.internal.DamageCauseVariable;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class DoHarmEffect
extends AdvancedEffect {
    public DoHarmEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "DO_HARM", "Harm an entity by doing health damage", "%e:<HEALTH>:<DAMAGE_CAUSE>");
        this.addArgument(0, Integer.class);
        this.addArgument(1, DamageCauseVariable.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        EntityDamageEvent.DamageCause damageCause = stringArray.length > 1 ? EntityDamageEvent.DamageCause.valueOf((String)stringArray[1].toUpperCase()) : EntityDamageEvent.DamageCause.CUSTOM;
        if (executionTask.getBuilder().isDamageEventNotGoingToRun()) {
            livingEntity.setMetadata("ae_damage_event_not_going_to_run", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
        }
        executionTask.getDamageHandler().damage((Damageable)livingEntity, (Entity)this.getOtherEntity(livingEntity, executionTask), -ASManager.parseInt(stringArray[0]), damageCause);
        if (executionTask.getBuilder().isDamageEventNotGoingToRun()) {
            livingEntity.removeMetadata("ae_damage_event_not_going_to_run", (Plugin)EffectsHandler.getInstance());
        }
        return true;
    }
}

