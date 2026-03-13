/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.targets;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class TargetResults {
    private List<LivingEntity> targetList;
    private List<Location> targetLocations;
    private String effect;

    public TargetResults(List<LivingEntity> list) {
        this.targetList = list;
    }

    public void mergeList(List<LivingEntity> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        ArrayList<LivingEntity> arrayList = new ArrayList<LivingEntity>(this.targetList == null ? new ArrayList() : this.targetList);
        for (LivingEntity livingEntity : list) {
            if (arrayList.contains(livingEntity)) continue;
            arrayList.add(livingEntity);
        }
        this.targetList = arrayList;
    }

    public void mergeLocations(List<Location> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        ArrayList<Location> arrayList = new ArrayList<Location>(this.targetLocations == null ? new ArrayList() : this.targetLocations);
        for (Location location : list) {
            if (arrayList.contains(location)) continue;
            arrayList.add(location);
        }
        this.targetLocations = arrayList;
    }

    private static String $default$effect() {
        return null;
    }

    public static TargetResultsBuilder builder() {
        return new TargetResultsBuilder();
    }

    public List<LivingEntity> getTargetList() {
        return this.targetList;
    }

    public List<Location> getTargetLocations() {
        return this.targetLocations;
    }

    public String getEffect() {
        return this.effect;
    }

    public TargetResults(List<LivingEntity> list, List<Location> list2, String string) {
        this.targetList = list;
        this.targetLocations = list2;
        this.effect = string;
    }

    public void setEffect(String string) {
        this.effect = string;
    }

    public static class TargetResultsBuilder {
        private List<LivingEntity> targetList;
        private List<Location> targetLocations;
        private boolean effect$set;
        private String effect$value;

        TargetResultsBuilder() {
        }

        public TargetResultsBuilder targetList(List<LivingEntity> list) {
            this.targetList = list;
            return this;
        }

        public TargetResultsBuilder targetLocations(List<Location> list) {
            this.targetLocations = list;
            return this;
        }

        public TargetResultsBuilder effect(String string) {
            this.effect$value = string;
            this.effect$set = true;
            return this;
        }

        public TargetResults build() {
            String string = this.effect$value;
            if (!this.effect$set) {
                string = TargetResults.$default$effect();
            }
            return new TargetResults(this.targetList, this.targetLocations, string);
        }

        public String toString() {
            return "TargetResults.TargetResultsBuilder(targetList=" + String.valueOf(this.targetList) + ", targetLocations=" + String.valueOf(this.targetLocations) + ", effect$value=" + this.effect$value + ")";
        }
    }
}

