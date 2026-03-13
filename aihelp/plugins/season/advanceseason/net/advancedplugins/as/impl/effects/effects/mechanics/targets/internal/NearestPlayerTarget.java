/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal;

import java.util.Collections;
import java.util.Comparator;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetArgument;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetType;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class NearestPlayerTarget
extends TargetType {
    public NearestPlayerTarget() {
        super("NearestPlayer");
    }

    @Override
    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        int n = ASManager.parseInt(this.classifyTarget(string2).getOrDefault((Object)TargetArgument.RADIUS, "0"));
        if (n == 0) {
            return this.noTarget();
        }
        LivingEntity livingEntity = executionTask.getBuilder().getMain();
        Location location = executionTask.getBuilder().getMain().getLocation();
        Entity entity2 = location.getWorld().getNearbyEntities(location, (double)n, (double)n, (double)n).stream().filter(entity -> !entity.equals((Object)livingEntity)).filter(entity -> entity instanceof Player).min(Comparator.comparing(entity -> entity.getLocation().distance(location))).orElse(null);
        if (entity2 == null || entity2.getLocation().distance(location) > (double)n) {
            return this.noTarget();
        }
        return new TargetResults(Collections.singletonList((Player)entity2));
    }
}

