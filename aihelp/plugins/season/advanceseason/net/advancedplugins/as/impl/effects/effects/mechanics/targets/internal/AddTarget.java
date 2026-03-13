/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal;

import java.util.Collections;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetArgument;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetType;
import org.bukkit.Location;

public class AddTarget
extends TargetType {
    public AddTarget() {
        super("Add");
    }

    @Override
    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        double d = Double.parseDouble(this.classifyTarget(string2).getOrDefault((Object)TargetArgument.X, "0.0"));
        double d2 = Double.parseDouble(this.classifyTarget(string2).getOrDefault((Object)TargetArgument.Y, "0.0"));
        double d3 = Double.parseDouble(this.classifyTarget(string2).getOrDefault((Object)TargetArgument.Z, "0.0"));
        Location location = executionTask.getBuilder().getMain().getLocation().clone().add(d, d2, d3);
        return TargetResults.builder().targetLocations(Collections.singletonList(location)).build();
    }
}

