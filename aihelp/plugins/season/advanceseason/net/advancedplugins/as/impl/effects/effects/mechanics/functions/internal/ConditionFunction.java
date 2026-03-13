/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.functions.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.conditions.ConResult;
import net.advancedplugins.as.impl.effects.effects.conditions.ConditionType;
import net.advancedplugins.as.impl.effects.effects.conditions.Fractor;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.FunctionType;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.LivingEntity;

public class ConditionFunction
implements FunctionType {
    @Override
    public String getName() {
        return "condition";
    }

    @Override
    public String parse(String string, LivingEntity livingEntity, ExecutionTask executionTask) {
        ConResult conResult = Fractor.getResult(string, executionTask.getBuilder().getAttacker(), executionTask.getBuilder().getVictim(), executionTask.getActionExecution());
        if (conResult.getOriginalCondition() == null) {
            ASManager.reportIssue(new NullPointerException(), "Invalid condition for function '" + string + "'");
            return "$skip";
        }
        if (conResult.getOriginalCondition() == ConditionType.ALLOW && conResult.getCondition() != ConditionType.ALLOW) {
            return "$skip";
        }
        if (conResult.getCondition() == ConditionType.STOP) {
            return "$skip";
        }
        return "";
    }
}

