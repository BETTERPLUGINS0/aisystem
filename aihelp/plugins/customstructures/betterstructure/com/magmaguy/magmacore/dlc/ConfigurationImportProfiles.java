/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package com.magmaguy.magmacore.dlc;

import com.magmaguy.magmacore.dlc.ConfigurationImporter;
import com.magmaguy.magmacore.util.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.bukkit.Bukkit;

final class ConfigurationImportProfiles {
    private ConfigurationImportProfiles() {
    }

    static Path resolve(ConfigurationImporter importer, String folder, ConfigurationImporter.PluginPlatform platform) {
        if (folder == null) {
            Logger.warn("Directory null for zipped file was not recognized! Was the zipped file packaged correctly?");
            return null;
        }
        if (platform == ConfigurationImporter.PluginPlatform.FREEMINECRAFTMODELS) {
            return importer.getFreeMinecraftModelsPath().resolve("models");
        }
        switch (folder) {
            case "custombosses": {
                if (!ConfigurationImportProfiles.isEliteMobsContent(platform) && !ConfigurationImportProfiles.isBetterStructuresContent(platform)) break;
                return importer.getEliteMobsPath().resolve("custombosses");
            }
            case "customitems": {
                if (!ConfigurationImportProfiles.isEliteMobsContent(platform) && !ConfigurationImportProfiles.isBetterStructuresContent(platform)) break;
                return importer.getEliteMobsPath().resolve("customitems");
            }
            case "customtreasurechests": {
                if (!ConfigurationImportProfiles.isEliteMobsContent(platform) && !ConfigurationImportProfiles.isBetterStructuresContent(platform)) break;
                return importer.getEliteMobsPath().resolve("customtreasurechests");
            }
            case "dungeonpackages": 
            case "content_packages": {
                if (platform == ConfigurationImporter.PluginPlatform.ELITEMOBS) {
                    return importer.getEliteMobsPath().resolve("content_packages");
                }
                if (platform == ConfigurationImporter.PluginPlatform.BETTERSTRUCTURES) {
                    return importer.getBetterStructuresPath().resolve("content_packages");
                }
                if (platform == ConfigurationImporter.PluginPlatform.EXTRACTIONCRAFT) {
                    return importer.getExtractioncraftPath().resolve("content_packages");
                }
                if (platform != ConfigurationImporter.PluginPlatform.MEGABLOCKSURVIVORS) break;
                return importer.getMegaBlockSurvivorsPath().resolve("content_packages");
            }
            case "customevents": {
                if (platform != ConfigurationImporter.PluginPlatform.ELITEMOBS) break;
                return importer.getEliteMobsPath().resolve("customevents");
            }
            case "customspawns": {
                if (platform != ConfigurationImporter.PluginPlatform.ELITEMOBS) break;
                return importer.getEliteMobsPath().resolve("customspawns");
            }
            case "customquests": {
                if (platform != ConfigurationImporter.PluginPlatform.ELITEMOBS && platform != ConfigurationImporter.PluginPlatform.EXTRACTIONCRAFT) break;
                return importer.getEliteMobsPath().resolve("customquests");
            }
            case "customarenas": {
                if (platform != ConfigurationImporter.PluginPlatform.ELITEMOBS) break;
                return importer.getEliteMobsPath().resolve("customarenas");
            }
            case "npcs": {
                if (platform == ConfigurationImporter.PluginPlatform.ELITEMOBS || platform == ConfigurationImporter.PluginPlatform.EXTRACTIONCRAFT) {
                    return importer.getEliteMobsPath().resolve("npcs");
                }
                if (platform != ConfigurationImporter.PluginPlatform.ETERNALTD) break;
                return importer.getEternalTDPath().resolve("npcs");
            }
            case "wormholes": {
                if (platform != ConfigurationImporter.PluginPlatform.ELITEMOBS) break;
                return importer.getEliteMobsPath().resolve("wormholes");
            }
            case "powers": {
                if (!ConfigurationImportProfiles.isEliteMobsContent(platform) && !ConfigurationImportProfiles.isBetterStructuresContent(platform)) break;
                return importer.getEliteMobsPath().resolve("powers");
            }
            case "worldcontainer": {
                try {
                    File worldContainer = Bukkit.getWorldContainer().getCanonicalFile();
                    return worldContainer.toPath().normalize().toAbsolutePath();
                } catch (IOException e) {
                    Logger.warn("Failed to resolve world container path canonically!");
                    e.printStackTrace();
                    return null;
                }
            }
            case "world_blueprints": {
                if (platform != ConfigurationImporter.PluginPlatform.ELITEMOBS) break;
                return importer.getEliteMobsPath().resolve("world_blueprints");
            }
            case "ModelEngine": 
            case "models": {
                importer.markModelsInstalled();
                if (Bukkit.getPluginManager().isPluginEnabled("FreeMinecraftModels")) {
                    return importer.getFreeMinecraftModelsPath().resolve("models");
                }
                if (Bukkit.getPluginManager().isPluginEnabled("ModelEngine")) {
                    return importer.getModelEnginePath().resolve("blueprints");
                }
                return importer.getFreeMinecraftModelsPath().resolve("models");
            }
            case "schematics": {
                if (platform == ConfigurationImporter.PluginPlatform.ELITEMOBS) {
                    Logger.warn("You just tried to import legacy content! Schematic dungeons no longer exist as of EliteMobs 9.0, use BetterStructures shrines instead!");
                    break;
                }
                if (platform == ConfigurationImporter.PluginPlatform.BETTERSTRUCTURES) {
                    return importer.getBetterStructuresPath().resolve("schematics");
                }
                if (platform != ConfigurationImporter.PluginPlatform.MEGABLOCKSURVIVORS) break;
                return importer.getMegaBlockSurvivorsPath().resolve("schematics");
            }
            case "levels": {
                if (platform != ConfigurationImporter.PluginPlatform.ETERNALTD) break;
                return importer.getEternalTDPath().resolve("levels");
            }
            case "waves": {
                if (platform != ConfigurationImporter.PluginPlatform.ETERNALTD) break;
                return importer.getEternalTDPath().resolve("waves");
            }
            case "worlds": {
                if (platform == ConfigurationImporter.PluginPlatform.ETERNALTD) {
                    return importer.getEternalTDPath().resolve("worlds");
                }
                if (platform != ConfigurationImporter.PluginPlatform.MEGABLOCKSURVIVORS) break;
                return importer.getMegaBlockSurvivorsPath().resolve("worlds");
            }
            case "elitemobs": {
                if (platform != ConfigurationImporter.PluginPlatform.BETTERSTRUCTURES) break;
                return importer.getEliteMobsPath();
            }
            case "pack.meta": {
                return null;
            }
            case "megablocksurvivors": {
                if (platform != ConfigurationImporter.PluginPlatform.MEGABLOCKSURVIVORS) break;
                return importer.getMegaBlockSurvivorsPath();
            }
            case "content_markers": {
                if (platform != ConfigurationImporter.PluginPlatform.MEGABLOCKSURVIVORS) break;
                return importer.getMegaBlockSurvivorsPath().resolve("content_markers");
            }
            case "spawn_pools": {
                if (!ConfigurationImportProfiles.isBetterStructuresContent(platform)) break;
                return importer.getBetterStructuresPath().resolve("spawn_pools");
            }
            case "components": {
                if (!ConfigurationImportProfiles.isBetterStructuresContent(platform)) break;
                return importer.getBetterStructuresPath().resolve("components");
            }
            case "modules": {
                if (!ConfigurationImportProfiles.isBetterStructuresContent(platform)) break;
                return importer.getBetterStructuresPath().resolve("modules");
            }
            case "module_generators": {
                if (!ConfigurationImportProfiles.isBetterStructuresContent(platform)) break;
                return importer.getBetterStructuresPath().resolve("module_generators");
            }
            case "loot_pools": {
                if (platform != ConfigurationImporter.PluginPlatform.EXTRACTIONCRAFT) break;
                return importer.getExtractioncraftPath().resolve("loot_pools");
            }
            case "loot_tables": {
                if (platform != ConfigurationImporter.PluginPlatform.EXTRACTIONCRAFT) break;
                return importer.getExtractioncraftPath().resolve("loot_tables");
            }
            case "resource_pack": {
                if (platform != ConfigurationImporter.PluginPlatform.ELITEMOBS) break;
                return importer.getEliteMobsPath().resolve("resource_pack");
            }
        }
        Logger.warn("Directory " + folder + " for zipped file was not recognized! Was the zipped file packaged correctly?");
        return null;
    }

    private static boolean isEliteMobsContent(ConfigurationImporter.PluginPlatform platform) {
        return platform == ConfigurationImporter.PluginPlatform.ELITEMOBS;
    }

    private static boolean isBetterStructuresContent(ConfigurationImporter.PluginPlatform platform) {
        return platform == ConfigurationImporter.PluginPlatform.BETTERSTRUCTURES || platform == ConfigurationImporter.PluginPlatform.EXTRACTIONCRAFT;
    }
}

