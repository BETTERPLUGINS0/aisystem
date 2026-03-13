/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.contentpackages.premade;

import com.magmaguy.betterstructures.config.contentpackages.ContentPackageConfigFields;
import java.util.List;

public class ExplorationPack
extends ContentPackageConfigFields {
    public ExplorationPack() {
        super("exploration_pack", true, "&2Exploration Pack", List.of((Object)"&fAdventure-focused pack of challenging structures!"), "https://nightbreak.io/plugin/betterstructures/#exploration-pack", "exploration");
        this.setContentPackageType(ContentPackageConfigFields.ContentPackageType.STRUCTURE);
        this.setNightbreakSlug("exploration-pack");
    }
}

