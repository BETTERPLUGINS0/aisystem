/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.contentpackages;

import com.magmaguy.betterstructures.config.contentpackages.ContentPackageConfigFields;
import com.magmaguy.betterstructures.content.BSPackage;
import com.magmaguy.magmacore.config.CustomConfig;
import java.util.HashMap;

public class ContentPackageConfig
extends CustomConfig {
    private static final HashMap<String, ContentPackageConfigFields> contentPackages = new HashMap();

    public ContentPackageConfig() {
        super("content_packages", "com.magmaguy.betterstructures.config.contentpackages.premade", ContentPackageConfigFields.class);
        contentPackages.clear();
        for (String key : super.getCustomConfigFieldsHashMap().keySet()) {
            contentPackages.put(key, (ContentPackageConfigFields)super.getCustomConfigFieldsHashMap().get(key));
            new BSPackage((ContentPackageConfigFields)super.getCustomConfigFieldsHashMap().get(key));
        }
    }

    public static HashMap<String, ContentPackageConfigFields> getContentPackages() {
        return contentPackages;
    }
}

