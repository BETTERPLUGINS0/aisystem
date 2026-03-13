/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package com.magmaguy.betterstructures.config.schematics;

import com.magmaguy.betterstructures.chests.ChestContents;
import com.magmaguy.betterstructures.config.generators.GeneratorConfig;
import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import com.magmaguy.betterstructures.config.treasures.TreasureConfig;
import com.magmaguy.betterstructures.config.treasures.TreasureConfigFields;
import com.magmaguy.magmacore.config.CustomConfigFields;
import com.magmaguy.magmacore.util.Logger;
import java.io.IOException;
import org.bukkit.Material;

public class SchematicConfigField
extends CustomConfigFields {
    private double weight = 1.0;
    private String generatorConfigFilename = "";
    private GeneratorConfigFields generatorConfigFields;
    private Material pedestalMaterial = null;
    private String treasureFile = null;
    private ChestContents chestContents = null;

    public SchematicConfigField(String filename, boolean isEnabled) {
        super(filename, isEnabled);
    }

    @Override
    public void processConfigFields() {
        this.isEnabled = this.processBoolean("isEnabled", this.isEnabled, true, true);
        this.weight = this.processDouble("weight", this.weight, 1.0, true);
        this.pedestalMaterial = this.processEnum("pedestalMaterial", this.pedestalMaterial, null, Material.class, false);
        this.generatorConfigFilename = this.processString("generatorConfigFilename", this.generatorConfigFilename, this.generatorConfigFilename, true);
        this.generatorConfigFields = GeneratorConfig.getConfigFields(this.generatorConfigFilename);
        this.treasureFile = this.processString("treasureFile", this.treasureFile, null, false);
        if (this.generatorConfigFields == null) {
            Logger.warn("Failed to assign a valid generator to " + this.filename + "! This will not spawn. Generator config name: " + this.generatorConfigFilename);
            return;
        }
        this.chestContents = this.generatorConfigFields.getChestContents();
        if (this.treasureFile != null && !this.treasureFile.isEmpty()) {
            TreasureConfigFields treasureConfigFields = TreasureConfig.getConfigFields(this.treasureFile);
            if (treasureConfigFields == null) {
                Logger.warn("Failed to get treasure config file " + this.treasureFile + " for schematic configuration " + this.filename + " ! Defaulting to the generator treasure.");
                return;
            }
            this.chestContents = treasureConfigFields.getChestContents();
        }
    }

    public void toggleEnabled(boolean enabled) {
        this.isEnabled = enabled;
        this.fileConfiguration.set("isEnabled", (Object)enabled);
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getGeneratorConfigFilename() {
        return this.generatorConfigFilename;
    }

    public void setGeneratorConfigFilename(String generatorConfigFilename) {
        this.generatorConfigFilename = generatorConfigFilename;
    }

    public GeneratorConfigFields getGeneratorConfigFields() {
        return this.generatorConfigFields;
    }

    public void setGeneratorConfigFields(GeneratorConfigFields generatorConfigFields) {
        this.generatorConfigFields = generatorConfigFields;
    }

    public Material getPedestalMaterial() {
        return this.pedestalMaterial;
    }

    public void setPedestalMaterial(Material pedestalMaterial) {
        this.pedestalMaterial = pedestalMaterial;
    }

    public String getTreasureFile() {
        return this.treasureFile;
    }

    public void setTreasureFile(String treasureFile) {
        this.treasureFile = treasureFile;
    }

    public ChestContents getChestContents() {
        return this.chestContents;
    }

    public void setChestContents(ChestContents chestContents) {
        this.chestContents = chestContents;
    }
}

