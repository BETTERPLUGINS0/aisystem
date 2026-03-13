/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal;

import java.util.Collections;
import java.util.List;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetType;

public class BlockTarget
extends TargetType {
    public BlockTarget() {
        super("Block");
    }

    @Override
    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        if (executionTask.getBuilder().getBlock() == null) {
            return TargetResults.builder().targetList(List.of()).build();
        }
        return TargetResults.builder().targetLocations(Collections.singletonList(executionTask.getBuilder().getBlock().getLocation())).build();
    }
}

