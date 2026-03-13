/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.generators;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import com.magmaguy.magmacore.config.CustomConfig;
import java.util.HashMap;

public class GeneratorConfig
extends CustomConfig {
    private static HashMap<String, GeneratorConfigFields> generatorConfigurations = new HashMap();

    public GeneratorConfig() {
        super("generators", "com.magmaguy.betterstructures.config.generators.premade", GeneratorConfigFields.class);
        generatorConfigurations.clear();
        for (String key : super.getCustomConfigFieldsHashMap().keySet()) {
            generatorConfigurations.put(key, (GeneratorConfigFields)super.getCustomConfigFieldsHashMap().get(key));
        }
    }

    public static GeneratorConfigFields getConfigFields(String configurationFilename) {
        return generatorConfigurations.get(configurationFilename);
    }

    public static HashMap<String, GeneratorConfigFields> getGeneratorConfigurations() {
        return generatorConfigurations;
    }
}

