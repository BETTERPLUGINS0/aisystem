/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.treasures;

import com.magmaguy.betterstructures.config.treasures.TreasureConfigFields;
import com.magmaguy.magmacore.config.CustomConfig;
import java.util.HashMap;

public class TreasureConfig
extends CustomConfig {
    private static HashMap<String, TreasureConfigFields> treasureConfigurations = new HashMap();

    public TreasureConfig() {
        super("treasures", "com.magmaguy.betterstructures.config.treasures.premade", TreasureConfigFields.class);
        treasureConfigurations.clear();
        for (String key : super.getCustomConfigFieldsHashMap().keySet()) {
            treasureConfigurations.put(key, (TreasureConfigFields)super.getCustomConfigFieldsHashMap().get(key));
        }
    }

    public static TreasureConfigFields getConfigFields(String configurationFilename) {
        return treasureConfigurations.get(configurationFilename);
    }

    public static HashMap<String, TreasureConfigFields> getTreasureConfigurations() {
        return treasureConfigurations;
    }
}

