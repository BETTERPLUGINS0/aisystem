/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.functions;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import org.bukkit.entity.LivingEntity;

public interface FunctionType {
    public String getName();

    public String parse(String var1, LivingEntity var2, ExecutionTask var3);
}

