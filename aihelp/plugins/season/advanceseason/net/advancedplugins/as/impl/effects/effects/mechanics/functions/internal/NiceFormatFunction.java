/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.FunctionType;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.LivingEntity;

public class NiceFormatFunction
implements FunctionType {
    @Override
    public String getName() {
        return "nice format";
    }

    @Override
    public String parse(String string, LivingEntity livingEntity, ExecutionTask executionTask) {
        return ASManager.formatMaterialName(string);
    }
}

