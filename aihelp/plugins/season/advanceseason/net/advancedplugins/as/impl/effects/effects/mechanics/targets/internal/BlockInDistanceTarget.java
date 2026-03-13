/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal;

import java.util.Collections;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetArgument;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetType;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class BlockInDistanceTarget
extends TargetType {
    public BlockInDistanceTarget() {
        super("BlockInDistance");
    }

    @Override
    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        int n = ASManager.parseInt(this.classifyTarget(string2).getOrDefault((Object)TargetArgument.DISTANCE, "1"));
        LivingEntity livingEntity = executionTask.getBuilder().getMain();
        Block block = executionTask.getBuilder().getMain().getTargetBlock(null, n);
        Location location = block == null ? livingEntity.getLocation().add(livingEntity.getLocation().getDirection().multiply(n)) : block.getLocation();
        return TargetResults.builder().targetLocations(Collections.singletonList(location)).build();
    }
}

