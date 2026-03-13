/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.util.Vector
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
import org.bukkit.util.Vector;

public class EntityInSightTarget
extends TargetType {
    public EntityInSightTarget() {
        super("EntityInSight");
    }

    public static List<LivingEntity> getEntityInLineOfSight(LivingEntity livingEntity, int n, int n2, int n3, AreaUtils.RadiusTarget radiusTarget) {
        Vector vector = livingEntity.getEyeLocation().getDirection();
        Vector vector2 = livingEntity.getEyeLocation().toVector();
        float f = (float)n2 / 100.0f;
        ArrayList<LivingEntity> arrayList = new ArrayList<LivingEntity>(n3);
        for (LivingEntity livingEntity2 : AreaUtils.getEntitiesInRadius(n, (Entity)livingEntity, radiusTarget)) {
            Vector vector3;
            Vector vector4;
            if (!livingEntity.hasLineOfSight((Entity)livingEntity2) || !(vector.angle(vector4 = (vector3 = livingEntity2.getLocation().toVector()).subtract(vector2)) < f)) continue;
            if (arrayList.size() < n3) {
                arrayList.add(livingEntity2);
                continue;
            }
            return arrayList;
        }
        return arrayList;
    }

    @Override
    public TargetResults getTargets(String string, String string2, ExecutionTask executionTask) {
        LivingEntity livingEntity = executionTask.getBuilder().getMain();
        HashMap<TargetArgument, String> hashMap = string2 != null ? this.classifyTarget(string2) : new HashMap<TargetArgument, String>();
        int n = ASManager.parseInt(hashMap.getOrDefault((Object)TargetArgument.DISTANCE, "20"));
        int n2 = ASManager.parseInt(hashMap.getOrDefault((Object)TargetArgument.ANGLE, "40"));
        int n3 = ASManager.parseInt(hashMap.getOrDefault((Object)TargetArgument.LIMIT, "5"));
        AreaUtils.RadiusTarget radiusTarget = AreaUtils.RadiusTarget.valueOf(hashMap.getOrDefault((Object)TargetArgument.TARGET, "ALL").toUpperCase(Locale.ROOT));
        return TargetResults.builder().targetList(EntityInSightTarget.getEntityInLineOfSight(livingEntity, n, n2, n3, radiusTarget)).build();
    }
}

