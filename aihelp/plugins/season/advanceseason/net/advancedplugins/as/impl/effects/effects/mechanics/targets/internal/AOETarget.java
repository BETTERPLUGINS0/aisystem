/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetArgument;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetType;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.AreaUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class AOETarget
extends TargetType {
    public AOETarget() {
        super("Aoe");
    }

    @Override
    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        if (string2 == null) {
            executionTask.reportIssue(string, "invalid targets");
            return TargetResults.builder().build();
        }
        HashMap<TargetArgument, String> hashMap = this.classifyTarget(string2);
        int n = ASManager.parseInt(hashMap.getOrDefault((Object)TargetArgument.RADIUS, "1"));
        int n2 = ASManager.parseInt(hashMap.getOrDefault((Object)TargetArgument.LIMIT, "20"));
        AreaUtils.RadiusTarget radiusTarget = AreaUtils.RadiusTarget.valueOf(hashMap.getOrDefault((Object)TargetArgument.TARGET, "ALL").toUpperCase(Locale.ROOT));
        List<Object> list = new ArrayList();
        LivingEntity livingEntity = executionTask.getBuilder().getMain();
        list = AreaUtils.getEntitiesInRadius(n, (Entity)livingEntity, radiusTarget);
        return TargetResults.builder().targetList(list.subList(0, Math.min(list.size(), n2))).build();
    }
}

