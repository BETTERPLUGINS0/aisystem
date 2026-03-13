/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package net.advancedplugins.as.impl.effects.effects.effects.utils;

import net.advancedplugins.as.impl.utils.CropUtils;
import org.bukkit.Material;

public enum PlantSeedsType {
    SEEDS("FARMLAND", "WHEAT_SEEDS", "WHEAT"),
    POTATO("FARMLAND", "POTATO", "POTATOES"),
    MELON("FARMLAND", "MELON_SEEDS", "MELON_STEM"),
    PUMPKIN("FARMLAND", "PUMPKIN_SEEDS", "PUMPKIN_STEM"),
    CARROT("FARMLAND", "CARROT", "CARROTS"),
    BEETROOT("FARMLAND", "BEETROOT_SEEDS", "BEETROOTS"),
    PITCHER_PLANT("FARMLAND", "PITCHER_POD", "PITCHER_CROP"),
    TORCHFLOWER_SEEDS("FARMLAND", "TORCHFLOWER_SEEDS", "TORCHFLOWER_CROP"),
    NETHER_WART("SOUL_SAND", "NETHER_WART", "NETHER_WART");

    private final Material blockRequired;
    private final Material seedsMaterial;
    private final Material plantedMaterial;

    private PlantSeedsType(String string2, String string3, String string4) {
        this.blockRequired = CropUtils.convertToMaterial(string2);
        this.seedsMaterial = CropUtils.convertToMaterial(string3);
        this.plantedMaterial = CropUtils.convertToMaterial(string4);
    }

    public Material getBlockRequired() {
        return this.blockRequired;
    }

    public Material getSeedsMaterial() {
        return this.seedsMaterial;
    }

    public Material getNewBlockType() {
        return this.plantedMaterial;
    }

    public static PlantSeedsType matchType(String string) {
        try {
            return PlantSeedsType.valueOf(string);
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }
}

