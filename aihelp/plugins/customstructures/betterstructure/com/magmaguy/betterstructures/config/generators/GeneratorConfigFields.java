/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World$Environment
 *  org.bukkit.block.Biome
 */
package com.magmaguy.betterstructures.config.generators;

import com.magmaguy.betterstructures.chests.ChestContents;
import com.magmaguy.betterstructures.config.treasures.TreasureConfig;
import com.magmaguy.betterstructures.config.treasures.TreasureConfigFields;
import com.magmaguy.magmacore.config.CustomConfigFields;
import com.magmaguy.magmacore.thirdparty.CustomBiomeCompatibility;
import com.magmaguy.magmacore.util.Logger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class GeneratorConfigFields
extends CustomConfigFields {
    private List<StructureType> structureTypes = new ArrayList<StructureType>(List.of((Object)((Object)StructureType.UNDEFINED)));
    private int lowestYLevel = -59;
    private int highestYLevel = 320;
    private List<String> validWorlds = null;
    private List<World.Environment> validWorldEnvironments = null;
    private List<String> validBiomesStrings = new ArrayList<String>();
    private List<String> validBiomesNamespaces = new ArrayList<String>();
    private String treasureFilename = null;
    private ChestContents chestContents = null;

    public GeneratorConfigFields(String filename, boolean isEnabled) {
        super(filename, isEnabled);
    }

    public GeneratorConfigFields(String filename, boolean isEnabled, List<StructureType> structureTypes) {
        super(filename, isEnabled);
        this.structureTypes = structureTypes;
    }

    @Override
    public void processConfigFields() {
        this.isEnabled = this.processBoolean("isEnabled", this.isEnabled, true, true);
        this.structureTypes = this.processEnumList("structureType", this.structureTypes, List.of((Object)((Object)StructureType.UNDEFINED)), StructureType.class, true);
        this.lowestYLevel = this.processInt("lowestYLevel", this.lowestYLevel, -59, false);
        this.highestYLevel = this.processInt("highestYLevel", this.highestYLevel, 320, false);
        this.validWorlds = this.processStringList("validWorlds", this.validWorlds, new ArrayList<String>(), false);
        this.validWorldEnvironments = this.processEnumList("validWorldEnvironments", this.validWorldEnvironments, null, World.Environment.class, false);
        this.processBiomes();
        this.treasureFilename = this.processString("treasureFilename", this.treasureFilename, null, false);
        TreasureConfigFields treasureConfig = TreasureConfig.getConfigFields(this.treasureFilename);
        if (treasureConfig != null) {
            this.chestContents = new ChestContents(treasureConfig);
        } else {
            Logger.warn("No valid treasure config file found for generator " + this.filename + " ! This will not spawn loot in chests until fixed.");
        }
    }

    private void processBiomes() {
        if (this.validBiomesNamespaces == null) {
            this.validBiomesNamespaces = new ArrayList<String>();
        } else {
            this.validBiomesNamespaces.clear();
        }
        if (this.fileConfiguration.contains("validBiomesV2") && !this.fileConfiguration.getList("validBiomesV2", new ArrayList()).isEmpty()) {
            this.validBiomesStrings = this.processStringList("validBiomesV2", this.validBiomesStrings, this.validBiomesStrings, false);
        }
        HashSet<String> processedBiomes = new HashSet<String>();
        ArrayList<String> standardizedBiomes = new ArrayList<String>();
        for (String string : this.validBiomesStrings) {
            String standardizedBiome = this.standardizeBiomeFormat(string);
            if (standardizedBiome == null) continue;
            standardizedBiomes.add(standardizedBiome);
            processedBiomes.add(standardizedBiome);
        }
        this.validBiomesNamespaces.addAll(standardizedBiomes);
        ArrayList<String> customBiomes = new ArrayList<String>();
        for (String standardizedBiome : standardizedBiomes) {
            if (!standardizedBiome.startsWith("minecraft:")) continue;
            List<String> mappedCustomBiomes = CustomBiomeCompatibility.getCustomBiomes(standardizedBiome);
            for (String customBiome : mappedCustomBiomes) {
                if (processedBiomes.contains(customBiome)) continue;
                customBiomes.add(customBiome);
                processedBiomes.add(customBiome);
            }
        }
        this.validBiomesNamespaces.addAll(customBiomes);
        if (customBiomes.size() > 0) {
            ArrayList<String> arrayList = new ArrayList<String>(this.validBiomesStrings);
            arrayList.addAll(customBiomes);
            this.validBiomesStrings = arrayList;
            this.fileConfiguration.set("validBiomesV2", arrayList);
        }
    }

    private String standardizeBiomeFormat(String biomeString) {
        if (biomeString == null || biomeString.isEmpty()) {
            return null;
        }
        if (biomeString.contains(":")) {
            return biomeString.toLowerCase(Locale.ROOT);
        }
        try {
            Biome biome = Biome.valueOf((String)biomeString.toUpperCase(Locale.ROOT));
            return "minecraft:" + biome.getKey().getKey();
        } catch (IllegalArgumentException e) {
            Logger.warn("Invalid biome name: " + biomeString);
            return null;
        }
    }

    public List<StructureType> getStructureTypes() {
        return this.structureTypes;
    }

    public void setStructureTypes(List<StructureType> structureTypes) {
        this.structureTypes = structureTypes;
    }

    public int getLowestYLevel() {
        return this.lowestYLevel;
    }

    public void setLowestYLevel(int lowestYLevel) {
        this.lowestYLevel = lowestYLevel;
    }

    public int getHighestYLevel() {
        return this.highestYLevel;
    }

    public void setHighestYLevel(int highestYLevel) {
        this.highestYLevel = highestYLevel;
    }

    public List<String> getValidWorlds() {
        return this.validWorlds;
    }

    public void setValidWorlds(List<String> validWorlds) {
        this.validWorlds = validWorlds;
    }

    public List<World.Environment> getValidWorldEnvironments() {
        return this.validWorldEnvironments;
    }

    public void setValidWorldEnvironments(List<World.Environment> validWorldEnvironments) {
        this.validWorldEnvironments = validWorldEnvironments;
    }

    public List<String> getValidBiomesStrings() {
        return this.validBiomesStrings;
    }

    public void setValidBiomesStrings(List<String> validBiomesStrings) {
        this.validBiomesStrings = validBiomesStrings;
    }

    public List<String> getValidBiomesNamespaces() {
        return this.validBiomesNamespaces;
    }

    public void setValidBiomesNamespaces(List<String> validBiomesNamespaces) {
        this.validBiomesNamespaces = validBiomesNamespaces;
    }

    public String getTreasureFilename() {
        return this.treasureFilename;
    }

    public void setTreasureFilename(String treasureFilename) {
        this.treasureFilename = treasureFilename;
    }

    public ChestContents getChestContents() {
        return this.chestContents;
    }

    public static enum StructureType {
        UNDEFINED,
        UNDERGROUND_DEEP,
        UNDERGROUND_SHALLOW,
        SURFACE,
        SKY,
        LIQUID_SURFACE,
        DUNGEON;

    }
}

