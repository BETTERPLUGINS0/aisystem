/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.treasures.premade;

import com.magmaguy.betterstructures.config.treasures.TreasureConfigFields;
import com.magmaguy.betterstructures.util.DefaultChestContents;

public class EndTreasureConfig
extends TreasureConfigFields {
    public EndTreasureConfig() {
        super("treasure_end", true);
        this.setRawLoot(DefaultChestContents.endContents());
    }
}

