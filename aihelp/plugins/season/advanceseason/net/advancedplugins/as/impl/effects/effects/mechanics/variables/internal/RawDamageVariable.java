/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageEvent
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.variables.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.VariableType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

public class RawDamageVariable
implements VariableType {
    @Override
    public String parse(String string, LivingEntity livingEntity, ExecutionTask executionTask) {
        if (!(executionTask.getBuilder().getEvent() instanceof EntityDamageEvent)) {
            return string;
        }
        EntityDamageEvent entityDamageEvent = (EntityDamageEvent)executionTask.getBuilder().getEvent();
        return string.replace(this.getVariable(), "" + (int)entityDamageEvent.getDamage());
    }

    @Override
    public String getVariable() {
        return "%raw damage%";
    }
}

