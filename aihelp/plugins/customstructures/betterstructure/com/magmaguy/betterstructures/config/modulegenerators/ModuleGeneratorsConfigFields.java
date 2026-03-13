/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World$Environment
 */
package com.magmaguy.betterstructures.config.modulegenerators;

import com.magmaguy.betterstructures.config.modules.ModulesConfig;
import com.magmaguy.betterstructures.config.modules.ModulesConfigFields;
import com.magmaguy.magmacore.config.CustomConfigFields;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.World;

public class ModuleGeneratorsConfigFields
extends CustomConfigFields {
    protected int radius;
    protected boolean edges;
    protected List<String> startModules;
    protected int minChunkY;
    protected int maxChunkY;
    protected int moduleSizeXZ;
    protected int moduleSizeY;
    protected boolean debug;
    protected boolean useGradientLevels;
    protected String spawnPoolSuffix;
    protected boolean isWorldGeneration;
    protected String treasureFile;
    private List<String> validWorlds = null;
    private List<World.Environment> validWorldEnvironments = null;
    protected int centerModuleAltitude = 0;

    public List<String> getStartModules() {
        ArrayList<String> existingModules = new ArrayList<String>();
        for (ModulesConfigFields value : ModulesConfig.getModuleConfigurations().values()) {
            if (!this.startModules.contains(value.getFilename().replace(".yml", ".schem"))) continue;
            existingModules.add(value.getFilename().replace(".yml", ".schem"));
        }
        return existingModules;
    }

    public ModuleGeneratorsConfigFields(String filename, boolean isEnabled) {
        super(filename, isEnabled);
    }

    public ModuleGeneratorsConfigFields(String filename) {
        super(filename, true);
    }

    @Override
    public void processConfigFields() {
        this.radius = this.processInt("radius", this.radius, 1, true);
        this.edges = this.processBoolean("edges", this.edges, false, true);
        this.startModules = this.processStringList("startModule", this.startModules, null, true);
        this.minChunkY = this.processInt("minChunkY", this.minChunkY, 0, true);
        this.maxChunkY = this.processInt("maxChunkY", this.maxChunkY, 0, true);
        this.moduleSizeXZ = this.processInt("moduleSizeXZ", this.moduleSizeXZ, 16, true);
        this.moduleSizeY = this.processInt("moduleSizeY", this.moduleSizeY, 16, true);
        this.debug = this.processBoolean("debug", this.debug, false, true);
        this.useGradientLevels = this.processBoolean("useGradientLevels", this.useGradientLevels, this.useGradientLevels, true);
        this.spawnPoolSuffix = this.processString("spawnPoolSuffix", this.spawnPoolSuffix, this.spawnPoolSuffix, true);
        this.isWorldGeneration = this.processBoolean("isWorldGeneration", this.isWorldGeneration, this.isWorldGeneration, true);
        this.treasureFile = this.processString("treasureFile", this.treasureFile, null, false);
        this.validWorlds = this.processStringList("validWorlds", this.validWorlds, new ArrayList<String>(), false);
        this.validWorldEnvironments = this.processEnumList("validWorldEnvironments", this.validWorldEnvironments, null, World.Environment.class, false);
        this.centerModuleAltitude = this.processInt("centerModuleAltitude", this.centerModuleAltitude, 0, false);
    }

    public int getRadius() {
        return this.radius;
    }

    public boolean isEdges() {
        return this.edges;
    }

    public int getMinChunkY() {
        return this.minChunkY;
    }

    public int getMaxChunkY() {
        return this.maxChunkY;
    }

    public int getModuleSizeXZ() {
        return this.moduleSizeXZ;
    }

    public int getModuleSizeY() {
        return this.moduleSizeY;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public boolean isUseGradientLevels() {
        return this.useGradientLevels;
    }

    public String getSpawnPoolSuffix() {
        return this.spawnPoolSuffix;
    }

    public boolean isWorldGeneration() {
        return this.isWorldGeneration;
    }

    public String getTreasureFile() {
        return this.treasureFile;
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

    public int getCenterModuleAltitude() {
        return this.centerModuleAltitude;
    }

    public void setCenterModuleAltitude(int centerModuleAltitude) {
        this.centerModuleAltitude = centerModuleAltitude;
    }
}

