/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.effects.effects.conditions;

import net.advancedplugins.as.impl.effects.effects.conditions.ConditionType;

public class ConResult {
    private final ConditionType c;
    private final ConditionType originalConditionType;
    private final Object rtrn;

    public ConResult(ConditionType conditionType, Object object, ConditionType conditionType2) {
        this.c = conditionType;
        this.originalConditionType = conditionType2;
        this.rtrn = object;
    }

    public ConditionType getOriginalCondition() {
        return this.originalConditionType;
    }

    public ConditionType getCondition() {
        return this.c;
    }

    public Object getResult() {
        switch (this.c) {
            case ADD: 
            case REMOVE: {
                return this.rtrn;
            }
            case FORCE: 
            case CONTINUE: 
            case STOP: 
            case ALLOW: {
                return this.c;
            }
        }
        return null;
    }
}

