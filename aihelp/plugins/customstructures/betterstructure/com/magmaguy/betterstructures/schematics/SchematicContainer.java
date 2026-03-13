/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.bukkit.BukkitAdapter
 *  com.sk89q.worldedit.extent.clipboard.Clipboard
 *  com.sk89q.worldedit.math.BlockVector3
 *  com.sk89q.worldedit.world.block.BaseBlock
 *  com.sk89q.worldedit.world.block.BlockState
 *  com.sk89q.worldedit.world.block.BlockType
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.EntityType
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.schematics;

import com.google.common.collect.ArrayListMultimap;
import com.magmaguy.betterstructures.chests.ChestContents;
import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import com.magmaguy.betterstructures.config.schematics.SchematicConfigField;
import com.magmaguy.betterstructures.config.treasures.TreasureConfig;
import com.magmaguy.betterstructures.config.treasures.TreasureConfigFields;
import com.magmaguy.betterstructures.schematics.AbstractBlock;
import com.magmaguy.betterstructures.util.WorldEditUtils;
import com.magmaguy.magmacore.util.Logger;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

public class SchematicContainer {
    private static final ArrayListMultimap<GeneratorConfigFields.StructureType, SchematicContainer> schematics = ArrayListMultimap.create();
    private final Clipboard clipboard;
    private final SchematicConfigField schematicConfigField;
    private final GeneratorConfigFields generatorConfigFields;
    private final String clipboardFilename;
    private final String configFilename;
    private final List<Vector> chestLocations = new ArrayList<Vector>();
    private final HashMap<Vector, EntityType> vanillaSpawns = new HashMap();
    private final HashMap<Vector, String> eliteMobsSpawns = new HashMap();
    private final HashMap<Vector, String> mythicMobsSpawns = new HashMap();
    List<AbstractBlock> abstractBlocks = new ArrayList<AbstractBlock>();
    private ChestContents chestContents = null;
    private boolean valid = true;

    public SchematicContainer(Clipboard clipboard, String clipboardFilename, SchematicConfigField schematicConfigField, String configFilename) {
        this.clipboard = clipboard;
        this.clipboardFilename = clipboardFilename;
        this.schematicConfigField = schematicConfigField;
        this.configFilename = configFilename;
        this.generatorConfigFields = schematicConfigField.getGeneratorConfigFields();
        if (this.generatorConfigFields == null) {
            Logger.warn("Failed to assign generator for configuration of schematic " + schematicConfigField.getFilename() + " ! This means this structure will not appear in the world.");
            return;
        }
        for (int x = 0; x <= clipboard.getDimensions().x(); ++x) {
            for (int y = 0; y <= clipboard.getDimensions().y(); ++y) {
                for (int z = 0; z <= clipboard.getDimensions().z(); ++z) {
                    BlockVector3 translatedLocation = BlockVector3.at((int)x, (int)y, (int)z).add(clipboard.getMinimumPoint());
                    BlockState weBlockState = clipboard.getBlock(translatedLocation);
                    Material minecraftMaterial = BukkitAdapter.adapt((BlockType)weBlockState.getBlockType());
                    if (minecraftMaterial == null) continue;
                    if (minecraftMaterial.equals((Object)Material.CHEST) || minecraftMaterial.equals((Object)Material.TRAPPED_CHEST) || minecraftMaterial.equals((Object)Material.SHULKER_BOX)) {
                        this.chestLocations.add(new Vector(x, y, z));
                    }
                    if (!minecraftMaterial.equals((Object)Material.ACACIA_SIGN) && !minecraftMaterial.equals((Object)Material.ACACIA_WALL_SIGN) && !minecraftMaterial.equals((Object)Material.SPRUCE_SIGN) && !minecraftMaterial.equals((Object)Material.SPRUCE_WALL_SIGN) && !minecraftMaterial.equals((Object)Material.BIRCH_SIGN) && !minecraftMaterial.equals((Object)Material.BIRCH_WALL_SIGN) && !minecraftMaterial.equals((Object)Material.CRIMSON_SIGN) && !minecraftMaterial.equals((Object)Material.CRIMSON_WALL_SIGN) && !minecraftMaterial.equals((Object)Material.DARK_OAK_SIGN) && !minecraftMaterial.equals((Object)Material.DARK_OAK_WALL_SIGN) && !minecraftMaterial.equals((Object)Material.JUNGLE_SIGN) && !minecraftMaterial.equals((Object)Material.JUNGLE_WALL_SIGN) && !minecraftMaterial.equals((Object)Material.OAK_SIGN) && !minecraftMaterial.equals((Object)Material.OAK_WALL_SIGN) && !minecraftMaterial.equals((Object)Material.WARPED_SIGN) && !minecraftMaterial.equals((Object)Material.WARPED_WALL_SIGN)) continue;
                    BaseBlock baseBlock = clipboard.getFullBlock(translatedLocation);
                    String line1 = WorldEditUtils.getLine(baseBlock, 1);
                    if (line1.toLowerCase(Locale.ROOT).contains("[spawn]")) {
                        EntityType entityType;
                        String line2 = WorldEditUtils.getLine(baseBlock, 2).toUpperCase(Locale.ROOT).replaceAll("\"", "");
                        try {
                            entityType = EntityType.valueOf((String)line2);
                        } catch (Exception ex) {
                            if (line2.equalsIgnoreCase("WITHER_CRYSTAL")) {
                                entityType = EntityType.END_CRYSTAL;
                            }
                            Logger.warn("Failed to determine entity type for sign! Entry was " + line2 + " in schematic " + clipboardFilename + " ! Fix this by inputting a valid entity type!");
                            continue;
                        }
                        this.vanillaSpawns.put(new Vector(x, y, z), entityType);
                        continue;
                    }
                    if (line1.toLowerCase(Locale.ROOT).contains("[elitemobs]")) {
                        if (Bukkit.getPluginManager().getPlugin("EliteMobs") == null) {
                            Bukkit.getLogger().warning("[BetterStructures] " + configFilename + " uses EliteMobs bosses but you do not have EliteMobs installed! BetterStructures does not require EliteMobs to work, but if you want cool EliteMobs boss fights you will have to install EliteMobs here: https://nightbreak.io/plugin/elitemobs/");
                            Bukkit.getLogger().warning("[BetterStructures] Since EliteMobs is not installed, " + configFilename + " will not be used.");
                            this.valid = false;
                            return;
                        }
                        Object filename = "";
                        for (int i = 2; i < 5; ++i) {
                            filename = (String)filename + WorldEditUtils.getLine(baseBlock, i);
                        }
                        this.eliteMobsSpawns.put(new Vector(x, y, z), (String)filename);
                        continue;
                    }
                    if (!line1.toLowerCase(Locale.ROOT).contains("[mythicmobs]")) continue;
                    if (Bukkit.getPluginManager().getPlugin("MythicMobs") == null) {
                        Bukkit.getLogger().warning("[BetterStructures] " + configFilename + " uses MythicMobs bosses but you do not have MythicMobs installed! BetterStructures does not require MythicMobs to work, but if you want MythicMobs boss fights you will have to install MythicMobs.");
                        Bukkit.getLogger().warning("[BetterStructures] Since MythicMobs is not installed, " + configFilename + " will not be used.");
                        this.valid = false;
                        return;
                    }
                    String mob = WorldEditUtils.getLine(baseBlock, 2);
                    String level = WorldEditUtils.getLine(baseBlock, 3);
                    this.mythicMobsSpawns.put(new Vector(x, y, z), mob + (String)(level.isEmpty() ? "" : ":" + level));
                }
            }
        }
        this.chestContents = this.generatorConfigFields.getChestContents();
        if (schematicConfigField.getTreasureFile() != null && !schematicConfigField.getTreasureFile().isEmpty()) {
            TreasureConfigFields treasureConfigFields = TreasureConfig.getConfigFields(schematicConfigField.getFilename());
            if (treasureConfigFields == null) {
                Logger.warn("Failed to get treasure configuration " + schematicConfigField.getTreasureFile());
                return;
            }
            this.chestContents = schematicConfigField.getChestContents();
        }
        if (this.valid) {
            this.generatorConfigFields.getStructureTypes().forEach(structureType -> schematics.put(structureType, (Object)this));
        }
    }

    public static void shutdown() {
        schematics.clear();
    }

    public boolean isValidEnvironment(World.Environment environment) {
        return this.generatorConfigFields.getValidWorldEnvironments() == null || this.generatorConfigFields.getValidWorldEnvironments().isEmpty() || this.generatorConfigFields.getValidWorldEnvironments().contains(environment);
    }

    public boolean isValidBiome(Object biomeObj) {
        if (this.generatorConfigFields.getValidBiomesNamespaces() == null) {
            return true;
        }
        if (this.generatorConfigFields.getValidBiomesNamespaces().isEmpty()) {
            return true;
        }
        String biomeString = this.getBiomeIdentifier(biomeObj);
        for (String validBiome : this.generatorConfigFields.getValidBiomesNamespaces()) {
            if (!biomeString.equals(validBiome)) continue;
            return true;
        }
        return false;
    }

    private String getBiomeIdentifier(Object biomeObj) {
        try {
            Method getKeyMethod = biomeObj.getClass().getMethod("getKey", new Class[0]);
            Object key = getKeyMethod.invoke(biomeObj, new Object[0]);
            Method getNamespaceMethod = key.getClass().getMethod("getNamespace", new Class[0]);
            Method getKeyNameMethod = key.getClass().getMethod("getKey", new Class[0]);
            String namespace = (String)getNamespaceMethod.invoke(key, new Object[0]);
            String keyName = (String)getKeyNameMethod.invoke(key, new Object[0]);
            return namespace + ":" + keyName;
        } catch (Exception e) {
            try {
                if (biomeObj.getClass().isEnum()) {
                    String enumName = ((Enum)biomeObj).name().toLowerCase();
                    return "minecraft:" + enumName;
                }
                Method nameMethod = biomeObj.getClass().getMethod("name", new Class[0]);
                String name = (String)nameMethod.invoke(biomeObj, new Object[0]);
                return "minecraft:" + name.toLowerCase();
            } catch (Exception e2) {
                String fallback = biomeObj.toString();
                if (fallback.contains("{") && fallback.contains("}")) {
                    int startIndex = fallback.indexOf("=") + 1;
                    int endIndex = fallback.indexOf("}", startIndex);
                    if (startIndex > 0 && endIndex > startIndex) {
                        fallback = fallback.substring(startIndex, endIndex);
                    }
                } else if (fallback.contains(".")) {
                    fallback = fallback.substring(fallback.lastIndexOf(".") + 1);
                }
                return "minecraft:" + fallback.toLowerCase().trim();
            }
        }
    }

    public boolean isValidYLevel(int yLevel) {
        return this.generatorConfigFields.getLowestYLevel() <= yLevel && this.generatorConfigFields.getHighestYLevel() >= yLevel;
    }

    public boolean isValidWorld(String worldName) {
        return this.generatorConfigFields.getValidWorlds() == null || this.generatorConfigFields.getValidWorlds().isEmpty() || this.generatorConfigFields.getValidWorlds().contains(worldName);
    }

    public static ArrayListMultimap<GeneratorConfigFields.StructureType, SchematicContainer> getSchematics() {
        return schematics;
    }

    public Clipboard getClipboard() {
        return this.clipboard;
    }

    public SchematicConfigField getSchematicConfigField() {
        return this.schematicConfigField;
    }

    public GeneratorConfigFields getGeneratorConfigFields() {
        return this.generatorConfigFields;
    }

    public String getClipboardFilename() {
        return this.clipboardFilename;
    }

    public String getConfigFilename() {
        return this.configFilename;
    }

    public List<Vector> getChestLocations() {
        return this.chestLocations;
    }

    public HashMap<Vector, EntityType> getVanillaSpawns() {
        return this.vanillaSpawns;
    }

    public HashMap<Vector, String> getEliteMobsSpawns() {
        return this.eliteMobsSpawns;
    }

    public HashMap<Vector, String> getMythicMobsSpawns() {
        return this.mythicMobsSpawns;
    }

    public List<AbstractBlock> getAbstractBlocks() {
        return this.abstractBlocks;
    }

    public ChestContents getChestContents() {
        return this.chestContents;
    }

    public boolean isValid() {
        return this.valid;
    }
}

