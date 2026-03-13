/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.variables.external;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.VariableType;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.LivingEntity;

public class EnchantLevelVariable
implements VariableType {
    @Override
    public String parse(String string, LivingEntity livingEntity, ExecutionTask executionTask) {
        int n = ASManager.parseInt(executionTask.getAbility().getName().substring(executionTask.getAbility().getName().lastIndexOf(" ")), 1);
        return string.replaceAll(this.getVariable(), "" + n);
    }

    @Override
    public String getVariable() {
        return "%level%";
    }
}

