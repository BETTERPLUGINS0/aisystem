/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Biome
 */
package com.magmaguy.betterstructures.config.modules;

import com.magmaguy.betterstructures.chests.ChestContents;
import com.magmaguy.betterstructures.config.modules.ModulesConfig;
import com.magmaguy.betterstructures.config.treasures.TreasureConfig;
import com.magmaguy.betterstructures.config.treasures.TreasureConfigFields;
import com.magmaguy.magmacore.config.CustomConfigFields;
import com.magmaguy.magmacore.util.Logger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.block.Biome;

public class ModulesConfigFields
extends CustomConfigFields {
    private String treasureFile = null;
    private ChestContents chestContents = null;
    private Map<String, Object> borderMap = new HashMap<String, Object>();
    private Integer minY = -4;
    private Integer maxY = 20;
    private boolean enforceVerticalRotation = false;
    private boolean enforceHorizontalRotation = false;
    private boolean noRepeat = false;
    private double weight = 100.0;
    private double repetitionPenalty = 0.0;
    private String moduleBiome = "default";
    private String minecraftBiomeString = "null";
    private Biome minecraftBiome = null;
    private String cloneConfig = "";
    private ModulesConfigFields clonedConfig = null;
    private boolean northIsPassable = true;
    private boolean southIsPassable = true;
    private boolean eastIsPassable = true;
    private boolean westIsPassable = true;
    private boolean upIsPassable = true;
    private boolean downIsPassable = true;
    private UUID uuid = UUID.randomUUID();
    private String compoundModule = null;
    private boolean isAutomaticallyPlaced = true;

    public UUID getUuid() {
        return this.clonedConfig == null ? this.uuid : this.clonedConfig.getUuid();
    }

    public String getCompoundModule() {
        return this.clonedConfig == null ? this.compoundModule : this.clonedConfig.getCompoundModule();
    }

    public ModulesConfigFields(String filename, boolean isEnabled) {
        super(filename, isEnabled);
    }

    public String getTreasureFile() {
        return this.clonedConfig == null ? this.treasureFile : this.clonedConfig.getTreasureFile();
    }

    public ChestContents getChestContents() {
        return this.clonedConfig == null ? this.chestContents : this.clonedConfig.getChestContents();
    }

    public Map<String, Object> getBorderMap() {
        return this.clonedConfig == null ? this.borderMap : this.clonedConfig.getBorderMap();
    }

    public Integer getMinY() {
        return this.clonedConfig == null ? this.minY : this.clonedConfig.getMinY();
    }

    public Integer getMaxY() {
        return this.clonedConfig == null ? this.maxY : this.clonedConfig.getMaxY();
    }

    public boolean isEnforceVerticalRotation() {
        return this.clonedConfig == null ? this.enforceVerticalRotation : this.clonedConfig.isEnforceVerticalRotation();
    }

    public boolean isEnforceHorizontalRotation() {
        return this.clonedConfig == null ? this.enforceHorizontalRotation : this.clonedConfig.isEnforceHorizontalRotation();
    }

    public boolean isNoRepeat() {
        return this.clonedConfig == null ? this.noRepeat : this.clonedConfig.isNoRepeat();
    }

    public double getWeight() {
        return this.clonedConfig == null ? this.weight : this.clonedConfig.getWeight();
    }

    public double getRepetitionPenalty() {
        return this.clonedConfig == null ? this.repetitionPenalty : this.clonedConfig.getRepetitionPenalty();
    }

    public boolean isNorthIsPassable() {
        return this.clonedConfig == null ? this.northIsPassable : this.clonedConfig.isNorthIsPassable();
    }

    public boolean isSouthIsPassable() {
        return this.clonedConfig == null ? this.southIsPassable : this.clonedConfig.isSouthIsPassable();
    }

    public boolean isEastIsPassable() {
        return this.clonedConfig == null ? this.eastIsPassable : this.clonedConfig.isEastIsPassable();
    }

    public boolean isWestIsPassable() {
        return this.clonedConfig == null ? this.westIsPassable : this.clonedConfig.isWestIsPassable();
    }

    public boolean isUpIsPassable() {
        return this.clonedConfig == null ? this.upIsPassable : this.clonedConfig.isUpIsPassable();
    }

    public boolean isDownIsPassable() {
        return this.clonedConfig == null ? this.downIsPassable : this.clonedConfig.isDownIsPassable();
    }

    public boolean isAutomaticallyPlaced() {
        return this.clonedConfig == null ? this.isAutomaticallyPlaced : this.clonedConfig.isAutomaticallyPlaced();
    }

    @Override
    public void processConfigFields() {
        this.isEnabled = this.processBoolean("isEnabled", this.isEnabled, true, true);
        this.treasureFile = this.processString("treasureFile", this.treasureFile, null, true);
        if (this.treasureFile != null && !this.treasureFile.isEmpty()) {
            TreasureConfigFields treasureConfigFields = TreasureConfig.getConfigFields(this.treasureFile);
            if (treasureConfigFields == null) {
                Logger.warn("Failed to get treasure config file " + this.treasureFile + " for schematic configuration " + this.filename + " ! Defaulting to the generator treasure.");
                return;
            }
            this.chestContents = treasureConfigFields.getChestContents();
        }
        this.borderMap = this.processMap("borders", new HashMap<String, Object>());
        this.minY = this.processInt("minY", this.minY, this.minY, true);
        this.maxY = this.processInt("maxY", this.maxY, this.maxY, true);
        this.enforceVerticalRotation = this.processBoolean("enforceVerticalRotation", this.enforceVerticalRotation, this.enforceVerticalRotation, true);
        this.noRepeat = this.processBoolean("noRepeat", this.noRepeat, this.noRepeat, true);
        this.weight = this.processDouble("weight", this.weight, this.weight, true);
        this.repetitionPenalty = this.processDouble("repetitionPenalty", this.repetitionPenalty, this.repetitionPenalty, true);
        this.enforceHorizontalRotation = this.processBoolean("enforceHorizontalRotation", this.enforceHorizontalRotation, this.enforceHorizontalRotation, true);
        this.moduleBiome = this.processString("biome", this.moduleBiome, this.moduleBiome, true);
        this.minecraftBiomeString = this.processString("minecraftBiome", this.minecraftBiomeString, this.minecraftBiomeString, true);
        if (!this.minecraftBiomeString.equalsIgnoreCase("null")) {
            try {
                this.minecraftBiome = Biome.valueOf((String)this.minecraftBiomeString.toUpperCase());
            } catch (Exception e) {
                Logger.warn("Biome " + this.minecraftBiomeString + " is not a valid biome! Fix it in " + this.filename);
            }
        }
        this.cloneConfig = this.processString("cloneConfig", this.cloneConfig, this.cloneConfig, true);
        this.compoundModule = this.processString("compoundModule", this.compoundModule, this.compoundModule, true);
        this.isAutomaticallyPlaced = this.processBoolean("isAutomaticallyPlaced", this.isAutomaticallyPlaced, this.isAutomaticallyPlaced, true);
    }

    public void validateClones() {
        if (this.cloneConfig.isEmpty()) {
            return;
        }
        this.clonedConfig = ModulesConfig.getModuleConfiguration(this.cloneConfig);
        if (this.clonedConfig == null) {
            Logger.warn("Configuration " + this.filename + " is supposed to clone " + String.valueOf(this.clonedConfig) + " but that is not a valid configuration file! The cloning setting will be ignored.");
            return;
        }
        this.fileConfiguration.set("treasureFile", null);
        this.fileConfiguration.set("borders", null);
        this.fileConfiguration.set("minY", null);
        this.fileConfiguration.set("maxY", null);
        this.fileConfiguration.set("enforceVerticalRotation", null);
        this.fileConfiguration.set("noRepeat", null);
        this.fileConfiguration.set("weight", null);
        this.fileConfiguration.set("repetitionPenalty", null);
        this.fileConfiguration.set("enforceHorizontalRotation", null);
        this.fileConfiguration.set("northIsPassable", null);
        this.fileConfiguration.set("southIsPassable", null);
        this.fileConfiguration.set("eastIsPassable", null);
        this.fileConfiguration.set("westIsPassable", null);
        this.fileConfiguration.set("upIsPassable", null);
        this.fileConfiguration.set("downIsPassable", null);
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTreasureFile(String treasureFile) {
        this.treasureFile = treasureFile;
    }

    public void setChestContents(ChestContents chestContents) {
        this.chestContents = chestContents;
    }

    public String getModuleBiome() {
        return this.moduleBiome;
    }

    public Biome getMinecraftBiome() {
        return this.minecraftBiome;
    }

    public String getCloneConfig() {
        return this.cloneConfig;
    }
}

