/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal;

import java.util.Collections;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetType;

public class EyeHeightTarget
extends TargetType {
    public EyeHeightTarget() {
        super("EyeHeight");
    }

    @Override
    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        return TargetResults.builder().targetLocations(Collections.singletonList(executionTask.getBuilder().getMain().getEyeLocation())).build();
    }
}

