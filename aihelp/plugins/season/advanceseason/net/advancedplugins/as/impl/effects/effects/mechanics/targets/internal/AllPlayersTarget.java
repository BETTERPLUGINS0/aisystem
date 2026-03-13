/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetType;
import org.bukkit.Bukkit;

public class AllPlayersTarget
extends TargetType {
    public AllPlayersTarget() {
        super("AllPlayers");
    }

    @Override
    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        return new TargetResults(Bukkit.getOnlinePlayers().stream().map(player -> player).toList());
    }
}

