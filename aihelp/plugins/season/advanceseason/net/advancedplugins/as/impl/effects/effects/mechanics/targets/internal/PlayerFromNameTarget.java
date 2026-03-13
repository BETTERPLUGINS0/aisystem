/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal;

import java.util.Collections;
import java.util.HashMap;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetArgument;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerFromNameTarget
extends TargetType {
    public PlayerFromNameTarget() {
        super("PlayerFromName");
    }

    @Override
    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        HashMap<TargetArgument, String> hashMap = this.classifyTarget(string2);
        String string3 = hashMap.get((Object)TargetArgument.TARGET);
        Player player = Bukkit.getPlayer((String)string3);
        if (player == null || !player.isOnline()) {
            executionTask.reportIssue(string, "invalid targets (player not found or offline)");
            return TargetResults.builder().build();
        }
        return new TargetResults(Collections.singletonList(player));
    }
}

