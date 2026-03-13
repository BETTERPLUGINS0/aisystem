/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.spawnpools;

import com.magmaguy.magmacore.config.CustomConfigFields;
import java.util.ArrayList;
import java.util.List;

public class SpawnPoolsConfigFields
extends CustomConfigFields {
    private List<String> poolStrings = new ArrayList<String>();
    private int minLevel = -1;
    private int maxLevel = -1;

    public SpawnPoolsConfigFields(String filename, boolean isEnabled) {
        super(filename, isEnabled);
    }

    @Override
    public void processConfigFields() {
        this.poolStrings = this.processStringList("poolStrings", this.poolStrings, this.poolStrings, true);
        this.minLevel = this.processInt("minLevel", this.minLevel, this.minLevel, false);
        this.maxLevel = this.processInt("maxLevel", this.maxLevel, this.maxLevel, false);
    }

    public List<String> getPoolStrings() {
        return this.poolStrings;
    }

    public int getMinLevel() {
        return this.minLevel;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }
}

