/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.variables.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.VariableType;
import org.bukkit.entity.LivingEntity;

public class AttackerNameVariable
implements VariableType {
    @Override
    public String parse(String string, LivingEntity livingEntity, ExecutionTask executionTask) {
        return string.replace(this.getVariable(), executionTask.getBuilder().getAttacker() != null ? executionTask.getBuilder().getAttacker().getName() : "");
    }

    @Override
    public String getVariable() {
        return "%attacker name%";
    }
}

