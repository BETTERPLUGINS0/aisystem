/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package com.magmaguy.betterstructures.buildingfitter.util;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import com.magmaguy.betterstructures.schematics.SchematicContainer;
import com.magmaguy.betterstructures.util.WeighedProbability;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Location;

public class SchematicPicker {
    public static SchematicContainer pick(Location naiveAnchorLocation, GeneratorConfigFields.StructureType structureType) {
        ArrayList<SchematicContainer> schematicContainers = new ArrayList<SchematicContainer>(SchematicContainer.getSchematics().get((Object)structureType));
        if (schematicContainers.isEmpty()) {
            return null;
        }
        schematicContainers.removeIf(schematicContainer -> !schematicContainer.isValidWorld(naiveAnchorLocation.getWorld().getName()) || !schematicContainer.isValidEnvironment(naiveAnchorLocation.getWorld().getEnvironment()) || !schematicContainer.isValidBiome(naiveAnchorLocation.getBlock().getBiome()) || !schematicContainer.isValidYLevel(naiveAnchorLocation.getBlockY()));
        if (schematicContainers.isEmpty()) {
            return null;
        }
        HashMap<Integer, Double> probabilities = new HashMap<Integer, Double>();
        for (int i = 0; i < schematicContainers.size(); ++i) {
            probabilities.put(i, ((SchematicContainer)schematicContainers.get(i)).getSchematicConfigField().getWeight());
        }
        SchematicContainer schematicContainer2 = (SchematicContainer)schematicContainers.get(WeighedProbability.pickWeightedProbability(probabilities));
        return schematicContainer2;
    }
}

