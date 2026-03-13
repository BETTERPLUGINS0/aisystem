/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.treasures.premade;

import com.magmaguy.betterstructures.config.treasures.TreasureConfigFields;
import com.magmaguy.betterstructures.util.DefaultChestContents;

public class NetherTreasureConfig
extends TreasureConfigFields {
    public NetherTreasureConfig() {
        super("treasure_nether", true);
        this.setRawLoot(DefaultChestContents.netherContents());
    }
}

