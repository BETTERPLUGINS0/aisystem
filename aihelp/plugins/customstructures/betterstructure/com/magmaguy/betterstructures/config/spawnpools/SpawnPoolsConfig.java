/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.spawnpools;

import com.magmaguy.betterstructures.config.spawnpools.SpawnPoolsConfigFields;
import com.magmaguy.magmacore.config.CustomConfig;
import java.util.HashMap;

public class SpawnPoolsConfig
extends CustomConfig {
    public static final HashMap<String, SpawnPoolsConfigFields> spawnPoolConfigFields = new HashMap();

    public SpawnPoolsConfig() {
        super("spawn_pools", "com.magmaguy.betterstructures.config.spawnpools.premade", SpawnPoolsConfigFields.class);
        spawnPoolConfigFields.clear();
        for (String key : super.getCustomConfigFieldsHashMap().keySet()) {
            spawnPoolConfigFields.put(key, (SpawnPoolsConfigFields)super.getCustomConfigFieldsHashMap().get(key));
        }
    }

    public static SpawnPoolsConfigFields getConfigFields(String configurationFilename) {
        return spawnPoolConfigFields.get(configurationFilename);
    }

    public static HashMap<String, SpawnPoolsConfigFields> getSpawnPoolConfigFields() {
        return spawnPoolConfigFields;
    }
}

