/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class IncreaseDamageEffect
extends AdvancedEffect {
    public IncreaseDamageEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "INCREASE_DAMAGE", "Increase damage by percentage", "%e:<AMOUNT>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (!(executionTask.getBuilder().getEvent() instanceof EntityDamageEvent)) {
            return false;
        }
        EntityDamageEvent entityDamageEvent = (EntityDamageEvent)executionTask.getBuilder().getEvent();
        double d = entityDamageEvent.getDamage();
        double d2 = Double.parseDouble(stringArray[0]) / 100.0;
        double d3 = d * d2;
        entityDamageEvent.setDamage(d + d3);
        return true;
    }
}

