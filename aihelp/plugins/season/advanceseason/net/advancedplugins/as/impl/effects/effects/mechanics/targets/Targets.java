/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetType;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.AOETarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.AddTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.AllPlayersTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.AttackerTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.BlockInDistanceTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.BlockTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.EntityInSightTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.EyeHeightTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.NearestPlayerTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.PlayerFromNameTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.SelfTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.TrenchTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.TunnelTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.VeinTarget;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.internal.VictimTarget;
import org.bukkit.entity.LivingEntity;

public class Targets {
    private final HashMap<String, TargetType> targetHandlers = new HashMap();

    public Targets() {
        this.register(new AttackerTarget());
        this.register(new VictimTarget());
        this.register(new BlockTarget());
        this.register(new VeinTarget());
        this.register(new NearestPlayerTarget());
        this.register(new TrenchTarget());
        this.register(new TunnelTarget());
        this.register(new EyeHeightTarget());
        this.register(new AddTarget());
        this.register(new AllPlayersTarget());
        this.register(new BlockInDistanceTarget());
        this.register(new AOETarget());
        this.register(new SelfTarget());
        this.register(new PlayerFromNameTarget());
        this.register(new EntityInSightTarget());
    }

    public void register(TargetType targetType) {
        this.targetHandlers.put(targetType.getName(), targetType);
    }

    public TargetResults handleTargets(String string, ExecutionTask executionTask) {
        Object object;
        ArrayList<TargetType> arrayList = new ArrayList<TargetType>();
        String[] stringArray = string.split("@");
        for (String object22 : stringArray) {
            TargetType targetType;
            if (object22.trim().isEmpty() || (targetType = this.targetHandlers.get(((String)(object = this.extractTargetName(object22))).trim())) == null) continue;
            arrayList.add(targetType);
        }
        if (arrayList.isEmpty()) {
            return new TargetResults(Collections.singletonList(executionTask.getBuilder().getMain()));
        }
        ArrayList arrayList2 = new ArrayList();
        for (TargetType targetType : arrayList) {
            String string2 = targetType.extractTargetString(string);
            object = targetType.getTargets(string, string2, executionTask);
            string = targetType.replaceTargetFromEffect(string);
            arrayList2.add(object);
        }
        TargetResults targetResults = new TargetResults(new ArrayList<LivingEntity>());
        Iterator iterator = arrayList2.iterator();
        while (iterator.hasNext()) {
            TargetResults targetResults2 = (TargetResults)iterator.next();
            targetResults2.setEffect(string);
            targetResults.mergeList(targetResults2.getTargetList());
            targetResults.mergeLocations(targetResults2.getTargetLocations());
        }
        targetResults.setEffect(string);
        return targetResults;
    }

    private String extractTargetName(String string) {
        int n = string.indexOf(123);
        if (n != -1) {
            return string.substring(0, n).trim();
        }
        int n2 = string.indexOf(32);
        if (n2 != -1) {
            return string.substring(0, n2).trim();
        }
        return string.trim();
    }

    public Collection<TargetType> targetTypes() {
        return this.targetHandlers.values();
    }
}

