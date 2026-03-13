/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.FunctionType;
import org.bukkit.entity.LivingEntity;

public class IfEndsFunction
implements FunctionType {
    @Override
    public String getName() {
        return "if ends";
    }

    @Override
    public String parse(String string, LivingEntity livingEntity, ExecutionTask executionTask) {
        if (!executionTask.getBuilder().isPermanent()) {
            return "";
        }
        if (!executionTask.getBuilder().isRemoved()) {
            return "";
        }
        return string;
    }
}

