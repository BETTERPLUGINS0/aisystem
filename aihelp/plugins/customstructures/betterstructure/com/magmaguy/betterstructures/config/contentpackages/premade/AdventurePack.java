/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.contentpackages.premade;

import com.magmaguy.betterstructures.config.contentpackages.ContentPackageConfigFields;
import java.util.List;

public class AdventurePack
extends ContentPackageConfigFields {
    public AdventurePack() {
        super("adventure_pack", true, "&2Adventure Pack", List.of((Object)"&f107 tough and massive adventure builds!"), "https://nightbreak.io/plugin/betterstructures/#adventure-pack", "adventure");
        this.setContentPackageType(ContentPackageConfigFields.ContentPackageType.STRUCTURE);
        this.setNightbreakSlug("adventure-pack");
    }
}

